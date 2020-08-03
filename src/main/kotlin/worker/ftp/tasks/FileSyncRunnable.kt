package app.fourdrin.sedai.worker.ftp.tasks

import app.fourdrin.sedai.*
import app.fourdrin.sedai.worker.loader.LoaderClient
import app.fourdrin.sedai.models.ftp.Account
import app.fourdrin.sedai.models.ftp.Manifest
import app.fourdrin.sedai.models.worker.FTPWork
import app.fourdrin.sedai.worker.FtpRunnable
import com.google.gson.Gson
import com.google.protobuf.ByteString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CopyObjectRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest

class FileSyncRunnable constructor(override val s3Client: S3Client, private val loaderClient: LoaderClient, val work: FTPWork) :
    FtpRunnable {

    lateinit var assetType:  LoaderServiceOuterClass.AssetType

    override fun run() {
        println("Syncing files...")
        // Open the manifest file
        val manifestRequest = GetObjectRequest.builder()
            .bucket(SEDAI_PIPELINE_DIRECTORY)
            .key(work.manifestS3Key)
            .build()

        val resp = s3Client.getObject(manifestRequest, ResponseTransformer.toBytes())
        val json = Gson().fromJson(String(resp.asByteArray()), Manifest::class.java)
        val account = json.accounts[work.accountName]

        // Move over things specified in the manifest file from this account's FTP folder to the data pipeline folder
        if (account != null) {
            runBlocking {
                syncFile(account).collect { s3Key ->
                    var metadataFile: ByteString? = null

                    if (s3Key.endsWith(".xml")) {
                        assetType = LoaderServiceOuterClass.AssetType.METADATA
                        metadataFile = ByteString.copyFrom(resp.asByteArray())
                    }

                    if (s3Key.endsWith(".epub")) {
                        assetType = LoaderServiceOuterClass.AssetType.EPUB
                    }

                    if (s3Key.endsWith(".jpg") || s3Key.endsWith(".jpeg")) {
                        assetType = LoaderServiceOuterClass.AssetType.COVER
                    }

                    // Kick off the load process
                    loaderClient.createLoad(
                        s3Key,
                        assetType,
                        metadataType = LoaderServiceOuterClass.MetadataType.UNKNOWN,
                        metadataFile = metadataFile
                    )
                }
            }

        }
    }

    private fun syncFile(account: Account): Flow<String> = flow {
        account.metadataFiles.forEach { metadataFile ->
            val source = "$SEDAI_FTP_ROOT_DIRECTORY/${metadataFile}"
            val destination = "${work.id}/${metadataFile}"
            // Copy the file over
            val copyRequest = CopyObjectRequest.builder()
                .copySource(source)
                .destinationBucket(SEDAI_PIPELINE_DIRECTORY)
                .destinationKey(destination)
                .build()

            try {
                s3Client.copyObject(copyRequest)

                // Delete it from the FTP folder.  This will prevent us from loading the same file multiple times and, instead,
                // only load files when the publisher _actually_ uploads them to the FTP server.

                // HACK: Skip if the account is the "internal" account.  Ideally, deleting should be configured based on environment
                if (account.name != SEDAI_INTERNAL_FTP_ACCOUNT_NAME) {
                    val deleteRequest = DeleteObjectRequest.builder()
                        .bucket(SEDAI_FTP_ROOT_DIRECTORY)
                        .key(metadataFile)
                        .build()

                    s3Client.deleteObject(deleteRequest)

                }

                emit(destination)
            } catch (e: Exception) {
            }
        }

        // TODO: Sync assets
    }
}