package app.fourdrin.sedai.loader

import io.grpc.ManagedChannel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.Closeable
import java.util.concurrent.TimeUnit


class LoaderClient constructor(private val channel: ManagedChannel) : Closeable {
    private val stub = LoaderServiceGrpcKt.LoaderServiceCoroutineStub(channel)

    suspend fun createLoad(s3Key: String, assetType: LoaderServiceOuterClass.AssetType) = coroutineScope {
        val request = LoaderServiceOuterClass.CreateLoadRequest.newBuilder()
            .setS3Key(s3Key)
            .setAssetType(assetType)
            .build()
        val response = async { stub.createLoad(request) }
        response.await()
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}