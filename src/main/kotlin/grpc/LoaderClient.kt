package app.fourdrin.sedai.grpc

import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.Closeable
import java.util.concurrent.TimeUnit


class LoaderClient constructor(private val channel: ManagedChannel) : Closeable {
    private val stub = LoaderServiceGrpcKt.LoaderServiceCoroutineStub(channel)

    suspend fun createLoad(s3Key: String, assetType: LoaderServiceOuterClass.AssetType, metadataType: LoaderServiceOuterClass.MetadataType, metadataFile: ByteString?) = coroutineScope {
        val request = LoaderServiceOuterClass.CreateLoadRequest.newBuilder()
            .setS3Key(s3Key)
            .setAssetType(assetType)
            .setMetadataType(metadataType)
            .setMetadataFile(metadataFile)
            .build()
        val resp = async { stub.createLoad(request) }
        println(resp.await())
    }

    override fun close() {
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
    }
}