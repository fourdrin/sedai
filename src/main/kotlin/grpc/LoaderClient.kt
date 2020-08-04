package app.fourdrin.sedai.grpc

import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.Closeable
import java.util.concurrent.TimeUnit


class LoaderClient constructor(private val channel: ManagedChannel) : Closeable {
    private val stub = LoaderServiceGrpcKt.LoaderServiceCoroutineStub(channel)

    suspend fun createFileSyncJob(id: String, accountName: String, manifestKey: String) = coroutineScope {
        val request = LoaderServiceOuterClass.CreateFileSyncJobRequest.newBuilder()
            .setId(id)
            .setAccountName(accountName)
            .setManifestKey(manifestKey)
            .build()

        val resp = async { stub.createFileSyncJob(request) }
        resp.await()
    }

    suspend fun createMetadataJob(s3Key: String,  metadataType: LoaderServiceOuterClass.MetadataType, metadataFile: ByteString?) = coroutineScope {
        val request = LoaderServiceOuterClass.CreateMetadataJobRequest.newBuilder()
            .setS3Key(s3Key)
            .setMetadataType(metadataType)
            .setMetadataFile(metadataFile)
            .build()
        val resp = async { stub.createMetadataJob(request) }
        resp.await()
    }

    suspend fun createEpubJob(s3Key: String, epubFile: ByteString) = coroutineScope {
        val request = LoaderServiceOuterClass.CreateEpubJobRequest.newBuilder()
            .setS3Key(s3Key)
            .setEpubFile(epubFile)
            .build()

        val resp = async { stub.createEpubJob(request) }
        resp.await()
    }

    suspend fun createCoverJob(s3Key: String, coverFile: ByteString) = coroutineScope {
        val request = LoaderServiceOuterClass.CreateCoverJobRequest.newBuilder()
            .setS3Key(s3Key)
            .setCoverFile(coverFile)
            .build()

        val resp = async { stub.createCoverJob(request) }
        resp.await()
    }


    override fun close() {
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
    }
}