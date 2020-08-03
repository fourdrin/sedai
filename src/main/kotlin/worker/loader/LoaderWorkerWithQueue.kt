package app.fourdrin.sedai.loader

import app.fourdrin.sedai.SEDAI_GRPC_SERVER_HOST
import app.fourdrin.sedai.SEDAI_GRPC_SERVER_PORT
import app.fourdrin.sedai.loader.tasks.MetadataRunnable
import app.fourdrin.sedai.loader.tasks.ParserRunnable
import app.fourdrin.sedai.models.metadata.CSVMetadata
import app.fourdrin.sedai.models.metadata.UnknownMetadata
import app.fourdrin.sedai.models.onix.parser.OnixParserStrategy
import app.fourdrin.sedai.models.worker.AssetType
import app.fourdrin.sedai.models.worker.LoaderWork
import app.fourdrin.sedai.models.worker.WorkerWithQueue
import app.fourdrin.sedai.worker.loader.LoaderClient
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object LoaderWorkerWithQueue : WorkerWithQueue<LoaderWork> {
    override val workerQueue = ConcurrentLinkedQueue<LoaderWork>()
    private val loaderExecutor = Executors.newSingleThreadScheduledExecutor()

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
        loaderExecutor.scheduleAtFixedRate({
            val work = workerQueue.poll()
            if (work != null) {
                if (work.assetType == AssetType.METADATA) {
                    when (work.metadataType) {
                        UnknownMetadata -> MetadataRunnable(s3Client, work.id, loaderClient).run()
                        CSVMetadata -> TODO()
                        else -> {
                            val strategy = OnixParserStrategy.build(work.metadataType)
                            ParserRunnable(strategy, work.metadataFile).run()
                        }
                    }
                }
            }
        }, 0, 5, TimeUnit.SECONDS)
    }

    override fun close() {
        println("Shut down")
        loaderExecutor.shutdown()
        loaderExecutor.awaitTermination(1, TimeUnit.MINUTES)
    }
}