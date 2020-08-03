package app.fourdrin.sedai.worker.job

import app.fourdrin.sedai.models.metadata.*
import app.fourdrin.sedai.models.onix.parser.OnixParserStrategy
import app.fourdrin.sedai.models.worker.AssetType
import app.fourdrin.sedai.models.worker.LoaderWork
import app.fourdrin.sedai.grpc.LoaderClient
import software.amazon.awssdk.services.s3.S3Client

object JobStrategy {
    fun build(s3Client: S3Client, loaderClient: LoaderClient, loaderJob: LoaderWork): Runnable {
        when (loaderJob.assetType) {
            AssetType.METADATA -> {
                return when (loaderJob.metadataType) {
                    UnknownMetadata -> MetadataRunnable(s3Client, loaderJob.id, loaderClient)
                    OnixThreeLong, OnixThreeShort, OnixTwoLong, OnixTwoShort -> {
                        val strategy = OnixParserStrategy.build(loaderJob.metadataType)
                        ParserRunnable(strategy, loaderJob.metadataFile)
                    }
                    CSVMetadata -> TODO()
                    else -> throw Exception("Unknown metadata type")
                }
            }
            else -> TODO()
        }
    }
}