package app.fourdrin.sedai.loader

import LoaderServiceGrpcKt
import LoaderServiceOuterClass
import app.fourdrin.sedai.models.AssetType
import app.fourdrin.sedai.models.LoaderWork
import app.fourdrin.sedai.models.UnknownMetadata

class LoaderService : LoaderServiceGrpcKt.LoaderServiceCoroutineImplBase() {
    override suspend fun createLoad(request: LoaderServiceOuterClass.CreateLoadRequest): LoaderServiceOuterClass.CreateLoadResponse {
        val assetType: AssetType = when(request.assetType) {
            LoaderServiceOuterClass.AssetType.UNRECOGNIZED -> AssetType.METADATA
            LoaderServiceOuterClass.AssetType.METADATA -> AssetType.METADATA
            LoaderServiceOuterClass.AssetType.EPUB -> AssetType.EPUB
            LoaderServiceOuterClass.AssetType.COVER -> AssetType.COVER
            else -> throw Exception("Unknown asset type")
        }

        val work = LoaderWork(
            id = request.s3Key,
            assetType = assetType,
            metadataType = UnknownMetadata
        )
        LoaderWorkerWithQueue.workerQueue.add(work)

        return LoaderServiceOuterClass.CreateLoadResponse.newBuilder().build()
    }
}

