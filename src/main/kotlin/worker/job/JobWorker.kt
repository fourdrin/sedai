package app.fourdrin.sedai.worker.job

import app.fourdrin.sedai.SEDAI_GRPC_SERVER_HOST
import app.fourdrin.sedai.SEDAI_GRPC_SERVER_PORT
import app.fourdrin.sedai.grpc.LoaderClient
import app.fourdrin.sedai.models.worker.LoaderWork
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

object JobWorker : Worker<LoaderWork> {
    override val queue = ConcurrentLinkedQueue<LoaderWork>()
    private val executor = Executors.newSingleThreadScheduledExecutor()
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
        executor.scheduleAtFixedRate({
            val job = queue.poll()
            if (job != null) {
                JobStrategy.build(
                    s3Client,
                    loaderClient, job).run()
            }
        }, 0, 5, TimeUnit.SECONDS)
    }

    override fun close() {
        executor.shutdown()
    }
}

