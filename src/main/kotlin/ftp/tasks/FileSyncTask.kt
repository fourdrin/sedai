package app.fourdrin.sedai.ftp.tasks

import app.fourdrin.sedai.ftp.FTP_ROOT_DIRECTORY
import app.fourdrin.sedai.ftp.FtpTask
import app.fourdrin.sedai.ftp.PIPELINE_DIRECTORY
import app.fourdrin.sedai.models.Account
import app.fourdrin.sedai.models.Manifest
import app.fourdrin.sedai.models.Work
import com.google.gson.Gson
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CopyObjectRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest

class FileSyncTask constructor(private val work: Work, private val s3Client: S3Client) : FtpTask(s3Client) {

    override fun run() {
        // Open the manifest file
        val manifestRequest = GetObjectRequest.builder()
            .bucket(PIPELINE_DIRECTORY)
            .key(work.manifestS3Key)
            .build()

        val resp = s3Client.getObject(manifestRequest, ResponseTransformer.toBytes())
        val json = Gson().fromJson(String(resp.asByteArray()), Manifest::class.java)
        val account = json.accounts[work.accountName]

        // Move over things specified in the manifest file from this account's FTP folder to the data pipeline folder
        // TODO: Emit the synced file to Kafka so downstream consumers can begin to process this file
        if (account != null) {
            runBlocking {
                syncFile(account).collect {
                        syncedFile -> println(syncedFile)
                }
            }
        }
    }

    private fun syncFile(account: Account): Flow<String> = flow {
        account.metadataFiles.forEach { metadataFile ->
            val source = "${FTP_ROOT_DIRECTORY}/${metadataFile}"
            val destination = "${work.id}/${metadataFile}"
            // Copy the file over
            val copyRequest = CopyObjectRequest.builder()
                .copySource(source)
                .destinationBucket(PIPELINE_DIRECTORY)
                .destinationKey(destination)
                .build()

            try {
                s3Client.copyObject(copyRequest)

                // Delete it from the FTP folder.  This will prevent us from loading the same file multiple times and, instead,
                // only load files when the publisher _actually_ uploads them to the FTP server.

                val deleteRequest = DeleteObjectRequest.builder()
                    .bucket(FTP_ROOT_DIRECTORY)
                    .key(metadataFile)
                    .build()

                s3Client.deleteObject(deleteRequest)

                emit("${PIPELINE_DIRECTORY}/${metadataFile}")
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}