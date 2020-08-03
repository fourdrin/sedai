package app.fourdrin.sedai.grpc

import LoaderServiceGrpcKt
import LoaderServiceOuterClass
import app.fourdrin.sedai.models.metadata.*
import app.fourdrin.sedai.models.worker.AssetType
import app.fourdrin.sedai.models.worker.LoaderWork
import app.fourdrin.sedai.worker.job.JobWorker
import java.io.ByteArrayInputStream

class LoaderService : LoaderServiceGrpcKt.LoaderServiceCoroutineImplBase() {
    override suspend fun createMetadataJob(request: LoaderServiceOuterClass.CreateMetadataJobRequest): LoaderServiceOuterClass.CreateMetadataJobResponse {
        val metadataType: MetadataType = when(request.metadataType) {
            LoaderServiceOuterClass.MetadataType.ONIX_TWO_LONG -> OnixTwoLong
            LoaderServiceOuterClass.MetadataType.ONIX_TWO_SHORT -> OnixTwoShort
            LoaderServiceOuterClass.MetadataType.ONIX_THREE_LONG -> OnixThreeLong
            LoaderServiceOuterClass.MetadataType.ONIX_THREE_SHORT -> OnixThreeShort
            LoaderServiceOuterClass.MetadataType.UNKNOWN -> UnknownMetadata
            else -> throw Exception("Unknown metadata type")
        }

        val metadataFile = ByteArrayInputStream(request.metadataFile.toByteArray())

        val job = LoaderWork(
            id = request.s3Key,
            assetType = AssetType.METADATA,
            metadataType = metadataType,
            metadataFile = metadataFile
        )

        JobWorker.queue.add(job)

        return LoaderServiceOuterClass.CreateMetadataJobResponse.newBuilder().setQueued(true).build()
    }
}

