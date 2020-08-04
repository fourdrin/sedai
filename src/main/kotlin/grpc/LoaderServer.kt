package app.fourdrin.sedai.grpc

import LoaderServiceGrpcKt
import LoaderServiceOuterClass
import app.fourdrin.sedai.ftp.FTPWorker
import app.fourdrin.sedai.models.metadata.*
import app.fourdrin.sedai.models.worker.FileType
import app.fourdrin.sedai.models.worker.FTPWork
import app.fourdrin.sedai.models.worker.LoaderWork
import app.fourdrin.sedai.worker.loader.LoaderWorker
import java.io.ByteArrayInputStream

class LoaderService : LoaderServiceGrpcKt.LoaderServiceCoroutineImplBase() {
    override suspend fun createFileSyncJob(request: LoaderServiceOuterClass.CreateFileSyncJobRequest): LoaderServiceOuterClass.CreateFileSyncJobResponse {
        val work = FTPWork(
            id = request.id,
            accountName = request.accountName,
            manifestKey =  request.manifestKey
        )

        FTPWorker.queue.add(work)

        return LoaderServiceOuterClass.CreateFileSyncJobResponse.newBuilder().setQueued(true).build()
    }

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
            fileType = FileType.METADATA,
            metadataType = metadataType,
            metadataFile = metadataFile
        )

        LoaderWorker.queue.add(job)

        return LoaderServiceOuterClass.CreateMetadataJobResponse.newBuilder().setQueued(true).build()
    }

    override suspend fun createEpubJob(request: LoaderServiceOuterClass.CreateEpubJobRequest): LoaderServiceOuterClass.CreateEpubJobResponse {
        // TODO

        return LoaderServiceOuterClass.CreateEpubJobResponse.newBuilder().setQueued(true).build()
    }

    override suspend fun createCoverJob(request: LoaderServiceOuterClass.CreateCoverJobRequest): LoaderServiceOuterClass.CreateCoverJobResponse {
        // TODO

        return LoaderServiceOuterClass.CreateCoverJobResponse.newBuilder().setQueued(true).build()
    }
}

