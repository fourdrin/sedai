package app.fourdrin.sedai.ftp

import app.fourdrin.sedai.SEDAI_GRPC_SERVER_HOST
import app.fourdrin.sedai.SEDAI_GRPC_SERVER_PORT
import app.fourdrin.sedai.worker.ftp.CheckpointRunnable
import app.fourdrin.sedai.worker.ftp.FileSyncRunnable
import app.fourdrin.sedai.grpc.LoaderClient
import app.fourdrin.sedai.models.worker.FTPWork
import app.fourdrin.sedai.worker.Worker
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


object FTPWorker : Worker<FTPWork> {
    private val checkpointExecutor = Executors.newSingleThreadScheduledExecutor()
    private val fileSyncExecutor = Executors.newSingleThreadScheduledExecutor()
    override val queue = ConcurrentLinkedQueue<FTPWork>()

    private val s3Client: S3Client = S3Client.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(ProfileCredentialsProvider.builder().profileName("default").build())
        .build()

    private val loaderClient = LoaderClient(
        ManagedChannelBuilder.forAddress(SEDAI_GRPC_SERVER_HOST, SEDAI_GRPC_SERVER_PORT)
            .usePlaintext()
            .executor(Dispatchers.Default.asExecutor())
            .build()
    )

    override fun start() {
        // Schedule checkpoint tasks, which kick off the book load process by getting the current "state" of the FTP server
        checkpointExecutor.scheduleAtFixedRate(CheckpointRunnable(s3Client) , 0, 60, TimeUnit.SECONDS)

        // Schedule a task to read from the work queue, which triggers moving files off the FTP server into the book load folder
        fileSyncExecutor.scheduleAtFixedRate({
            val work: FTPWork? = queue.poll()
            if (work != null) {
                FileSyncRunnable(s3Client, loaderClient, work).run()
            }
        }, 0, 10, TimeUnit.SECONDS)
    }

    override fun close() {
        checkpointExecutor.awaitTermination(1, TimeUnit.MINUTES)
        fileSyncExecutor.awaitTermination(10, TimeUnit.SECONDS)
    }
}

