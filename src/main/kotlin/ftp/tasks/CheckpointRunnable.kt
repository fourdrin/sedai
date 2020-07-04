package app.fourdrin.sedai.ftp.tasks

import app.fourdrin.sedai.ftp.FTPWorkerWithQueue
import app.fourdrin.sedai.ftp.FTP_ROOT_DIRECTORY
import app.fourdrin.sedai.ftp.FtpRunnable
import app.fourdrin.sedai.models.*
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Object
import java.time.Instant
import java.util.concurrent.ConcurrentLinkedQueue

const val ASSET_REGEX = ".(jpg|jpeg|epub)"
const val MANIFEST_NAME = "manifest.json"

class CheckpointRunnable constructor(override val s3Client: S3Client) :
    FtpRunnable {

    override fun run() {
        runBlocking {
            val accounts = mutableMapOf<String, Account>()

            // Get all the top-level accounts on our "ftp" server
            val accountsRequest = ListObjectsV2Request.builder()
                .bucket(FTP_ROOT_DIRECTORY)
                .delimiter("/")
                .build()

            val accountKeys: List<String> = s3Client.listObjectsV2(accountsRequest)
                .commonPrefixes()
                .map { p -> p.prefix().replace("/", "") }


            // Establish a checkpoint by creating a manifest file using ${epoch}/manifest.json as the key
            // The manifest file will represent the current "state" of the FTP server for each account
            val startedAt = Instant.now()

            // The ID for this load is just the UNIX timestamp
            val id = startedAt.epochSecond.toString()

            // Set the key for the generated manifest file
            val manifestKey = "${id}/${MANIFEST_NAME}"

            // For each account, use a coroutine to record the current state of asset files and metadata files (e.g epubs and ONIX files)
            val job = launch {
                accountKeys.forEach { accountKey ->
                    launch {
                        val account = async { buildAccount(accountKey) }
                        accounts[accountKey] = account.await()
                    }
                }
            }
            job.join()


            val manifest = Manifest(id, startedAt.toString(), accounts.toMap())
            val manifestJSON = Gson().toJson(manifest)

            val manifestRequest = PutObjectRequest.builder()
                .bucket("books-data-pipeline")
                .contentType("application/json")
                .key(manifestKey)
                .build()

            // Upload the manifest
            val upload = async { s3Client.putObject(manifestRequest, RequestBody.fromString(manifestJSON)) }
            upload.await()

            // For each "account", create a unit of Work and queue it up
            accountKeys.forEach { accountKey ->
                val account = accounts[accountKey]

                if (account != null) {
                    val work = FTPWork(
                        id,
                        accountName = accountKey,
                        manifestName = MANIFEST_NAME
                    )

                    FTPWorkerWithQueue.workerQueue.add(work)
                }
            }
        }
    }

    private fun buildAccount(accountKey: String): Account = runBlocking {
        // Get all the files in this bucket
        // We do this before we sort out assets and metadata to avoid excessive AWS API calls
        val s3Objects = mutableListOf<S3Object>()

        do {
            var nextContinuationToken: String? = null
            val listRequest = ListObjectsV2Request.builder()
                .bucket(
                    FTP_ROOT_DIRECTORY
                )
                .prefix(accountKey)
                .continuationToken(nextContinuationToken)
                .build()

            val listResponse = s3Client.listObjectsV2(listRequest)
            s3Objects.addAll(listResponse.contents())

            nextContinuationToken = listResponse.nextContinuationToken()
        } while (nextContinuationToken != null)

        val metadata = async { buildMetadataFiles(accountKey, s3Objects) }
        val assets = async { buildAssetFiles(accountKey, s3Objects) }

        val metadataFiles = metadata.await()
        val assetFiles = assets.await()

        Account(
            accountKey,
            metadataFiles,
            assetFiles
        )
    }
}

private suspend fun buildMetadataFiles(accountKey: String, s3Objects: List<S3Object>): List<String?> {
    return s3Objects.map { s3Object -> s3Object.key() }
        .filter { key ->
            key != "${accountKey}/"
        }
        .filter { key ->
            key.endsWith(".xml")
        }
}

private suspend fun buildAssetFiles(accountKey: String, s3Objects: List<S3Object>): Map<String, Asset> {
    val matcher = Regex(ASSET_REGEX)
    val assets = mutableMapOf<String, MutableMap<AssetType, String?>>()

    // S3 key includes the trailing slash
    val accountS3Key = "${accountKey}/"

    s3Objects
        .map { s3Object -> s3Object.key() }
        .filter { key ->
            key != accountS3Key && !key.endsWith(".xml")
        }
        .forEach() { key ->
            val isbn = key.replace(matcher, "").replace(accountS3Key, "")
            val assetType =  if (key.matches(matcher)) AssetType.COVER else AssetType.EPUB

            // Check if we've seen this asset before (i.e. the cover but not the epub and vice versa).
            // If we have, we'll use the previous value.  Otherwise, create a new map since is the first time we've seen this asset
            val asset = assets[isbn] ?: mutableMapOf<AssetType, String?>(AssetType.EPUB to null, AssetType.COVER to null)

            // Add the asset type and associate the key
            asset[assetType] = key

            // Update the map of assets with this ISBN and asset
            assets[isbn] = asset
        }

    return assets.toMap()
}
