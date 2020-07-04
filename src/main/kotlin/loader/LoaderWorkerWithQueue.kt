package app.fourdrin.sedai.loader

import app.fourdrin.sedai.loader.tasks.MetadataRunnable
import app.fourdrin.sedai.loader.tasks.ParserRunnable
import app.fourdrin.sedai.models.LoaderWork
import app.fourdrin.sedai.models.MetadataVersion
import app.fourdrin.sedai.models.WorkerWithQueue
import app.fourdrin.sedai.onix.VersionThreeParserStrategy
import app.fourdrin.sedai.onix.VersionTwoParserStrategy
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object LoaderWorkerWithQueue : WorkerWithQueue<LoaderWork> {
    override val workerQueue = ConcurrentLinkedQueue<LoaderWork>()
    private val loaderExecutor = Executors.newSingleThreadScheduledExecutor()

    // Parsers
    private val versionTwoParserStrategy = VersionTwoParserStrategy()
    private val versionThreeParserStrategy = VersionThreeParserStrategy()

    private val s3Client: S3Client = S3Client.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(ProfileCredentialsProvider.builder().profileName("default").build())
        .build()

    override fun start() {
        loaderExecutor.scheduleAtFixedRate({
            val work = workerQueue.poll()

            if (work != null) {
                when(work.metadataVersion) {
                    MetadataVersion.UNKNOWN -> MetadataRunnable(s3Client, work.id).run()
                    MetadataVersion.TWO -> ParserRunnable(versionTwoParserStrategy, "")
                    MetadataVersion.THREE -> ParserRunnable(versionThreeParserStrategy, "")
                }
            }
        } , 0, 5, TimeUnit.SECONDS)
    }

    override fun close() {
        loaderExecutor.shutdown()
    }
}