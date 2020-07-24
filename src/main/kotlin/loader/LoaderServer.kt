package app.fourdrin.sedai.loader

import LoaderServiceGrpcKt
import LoaderServiceOuterClass
import app.fourdrin.sedai.models.*
import java.io.ByteArrayInputStream
import java.io.InputStream

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
            else -> throw Exception("Unknown asset type")
        }

        val metadataFile = ByteArrayInputStream(request.metadataFile.toByteArray())

        val work = LoaderWork(
            id = request.s3Key,
            assetType = assetType,
            metadataType = metadataType,
            metadataFile = metadataFile
        )

        LoaderWorkerWithQueue.workerQueue.add(work)

        return LoaderServiceOuterClass.CreateLoadResponse.newBuilder().build()
    }
}

