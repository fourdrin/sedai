package app.fourdrin.sedai.worker

import app.fourdrin.sedai.loader.tasks.MetadataRunnable
import app.fourdrin.sedai.loader.tasks.ParserRunnable
import app.fourdrin.sedai.models.metadata.*
import app.fourdrin.sedai.models.onix.parser.OnixParserStrategy
import app.fourdrin.sedai.models.worker.AssetType
import app.fourdrin.sedai.models.worker.Job
import app.fourdrin.sedai.worker.loader.LoaderClient
import software.amazon.awssdk.services.s3.S3Client

object JobStrategy {
    fun build(s3Client: S3Client, loaderClient: LoaderClient, job: Job): Runnable {
        when (job.assetType) {
            AssetType.METADATA -> {
                return when (job.metadataType) {
                    UnknownMetadata -> MetadataRunnable(s3Client, job.id, loaderClient)
                    OnixThreeLong, OnixThreeShort, OnixTwoLong, OnixTwoShort -> {
                        val strategy = OnixParserStrategy.build(job.metadataType)
                        ParserRunnable(strategy, job.metadataFile)
                    }
                    CSVMetadata -> TODO()
                    else -> throw Exception("Unknown metadata type")
                }
            }
            else -> TODO()
        }
    }
}