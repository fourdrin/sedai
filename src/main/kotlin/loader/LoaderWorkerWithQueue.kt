package app.fourdrin.sedai.loader

import app.fourdrin.sedai.loader.tasks.MetadataRunnable
import app.fourdrin.sedai.loader.tasks.ParserRunnable
import app.fourdrin.sedai.models.AssetType
import app.fourdrin.sedai.models.LoaderWork
import app.fourdrin.sedai.models.WorkerWithQueue
import app.fourdrin.sedai.models.onix.Unknown
import app.fourdrin.sedai.models.onix.parser.OnixParserStrategy
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

    override fun start() {
        loaderExecutor.scheduleAtFixedRate({
            val work = workerQueue.poll()
            if (work != null) {
                if (work.assetType == AssetType.METADATA) {
                    when (work.metadataVersion) {
                        Unknown -> MetadataRunnable(s3Client, work.id).run()
                        else -> ParserRunnable(OnixParserStrategy.build(work.metadataVersion), work.metadataFile).run()
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