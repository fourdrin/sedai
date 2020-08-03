package app.fourdrin.sedai.worker.loader

import LoaderServiceGrpcKt
import LoaderServiceOuterClass
import app.fourdrin.sedai.models.metadata.*
import app.fourdrin.sedai.models.worker.AssetType
import app.fourdrin.sedai.models.worker.Job
import app.fourdrin.sedai.models.worker.LoaderWork
import app.fourdrin.sedai.worker.JobWorker
import java.io.ByteArrayInputStream

class LoaderService : LoaderServiceGrpcKt.LoaderServiceCoroutineImplBase() {
    override suspend fun createLoad(request: LoaderServiceOuterClass.CreateLoadRequest): LoaderServiceOuterClass.CreateLoadResponse {
        val assetType: AssetType = when(request.assetType) {
            LoaderServiceOuterClass.AssetType.UNRECOGNIZED -> AssetType.METADATA
            LoaderServiceOuterClass.AssetType.METADATA -> AssetType.METADATA
            LoaderServiceOuterClass.AssetType.EPUB -> AssetType.EPUB
            LoaderServiceOuterClass.AssetType.COVER -> AssetType.COVER
            else -> throw Exception("Unknown asset type")
        }

        val metadataType: MetadataType = when(request.metadataType) {
            LoaderServiceOuterClass.MetadataType.ONIX_TWO_LONG -> OnixTwoLong
            LoaderServiceOuterClass.MetadataType.ONIX_TWO_SHORT -> OnixTwoShort
            LoaderServiceOuterClass.MetadataType.ONIX_THREE_LONG -> OnixThreeLong
            LoaderServiceOuterClass.MetadataType.ONIX_THREE_SHORT -> OnixThreeShort
            LoaderServiceOuterClass.MetadataType.UNKNOWN -> UnknownMetadata
            else -> throw Exception("Unknown asset type")
        }

        val metadataFile = ByteArrayInputStream(request.metadataFile.toByteArray())

        val job = Job(
            id = request.s3Key,
            assetType = assetType,
            metadataType = metadataType,
            metadataFile = metadataFile
        )

        JobWorker.queue.add(job)

        return LoaderServiceOuterClass.CreateLoadResponse.newBuilder().build()
    }
}

