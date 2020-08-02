package worker.loader.tasks

import LoaderServiceGrpcKt
import LoaderServiceOuterClass
import app.fourdrin.sedai.loader.LoaderClient
import app.fourdrin.sedai.loader.tasks.MetadataRunnable
import com.google.protobuf.ByteString
import com.nhaarman.mockitokotlin2.*
import io.grpc.Server
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.testing.GrpcCleanupRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers
import org.mockito.AdditionalAnswers.delegatesTo
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import software.amazon.awssdk.core.ResponseBytes
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectResponse
import java.time.Instant

internal class MetadataRunnableTest {
    private val metadataKey = "${Instant.now().epochSecond.toString()}/internal/onix.xml"
    private val twoLongInputStream = this.javaClass.getResourceAsStream("/onix/2L.xml").readBytes()
    private val twoShortInputStream = this.javaClass.getResourceAsStream("/onix/2S.xml").readBytes()

    private val s3Client = Mockito.mock(S3Client::class.java)
    private val loaderService = Mockito.mock(
        LoaderServiceGrpcKt.LoaderServiceCoroutineImplBase::class.java,
        delegatesTo<LoaderServiceGrpcKt.LoaderServiceCoroutineImplBase>(
            object : LoaderServiceGrpcKt.LoaderServiceCoroutineImplBase() {
                override suspend fun createLoad(request: LoaderServiceOuterClass.CreateLoadRequest): LoaderServiceOuterClass.CreateLoadResponse {
                    return LoaderServiceOuterClass.CreateLoadResponse.newBuilder().build()
                }
            }
        )
    )

    private val serverName = InProcessServerBuilder.generateName()
    private val grpcServer = InProcessServerBuilder
        .forName(serverName)
        .directExecutor()
        .addService(loaderService)
        .build()
        .start()

    private val grpcCleanup = GrpcCleanupRule()

    init {
        grpcCleanup.register(grpcServer)
    }

    private val channel = grpcCleanup.register(
        InProcessChannelBuilder.forName(serverName).directExecutor().build()
    )
    private val loaderClient = Mockito.mock(LoaderClient::class.java)

    private val metadataTypeCaptor = ArgumentCaptor.forClass(LoaderServiceOuterClass.MetadataType::class.java)

    @Test
    fun testRunOnixTwoLong() = runBlocking {
        Mockito.`when`(
            s3Client.getObject(
                any<GetObjectRequest>(),
                any<ResponseTransformer<GetObjectResponse, ResponseBytes<GetObjectResponse>>>()
            )
        ).thenReturn(
            ResponseBytes.fromByteArray(GetObjectResponse.builder().build(), twoLongInputStream)
        )

        MetadataRunnable(s3Client, metadataKey, loaderClient).run()

        verify(loaderClient).createLoad(
            eq(metadataKey),
            eq(LoaderServiceOuterClass.AssetType.METADATA),
            eq(LoaderServiceOuterClass.MetadataType.ONIX_TWO_LONG),
            any()
        )
    }

    @Test
    fun testRunOnixTwoShort() = runBlocking {
        Mockito.`when`(
            s3Client.getObject(
                any<GetObjectRequest>(),
                any<ResponseTransformer<GetObjectResponse, ResponseBytes<GetObjectResponse>>>()
            )
        ).thenReturn(
            ResponseBytes.fromByteArray(GetObjectResponse.builder().build(), twoShortInputStream)
        )

        MetadataRunnable(s3Client, metadataKey, loaderClient).run()

        verify(loaderClient).createLoad(
            eq(metadataKey),
            eq(LoaderServiceOuterClass.AssetType.METADATA),
            eq(LoaderServiceOuterClass.MetadataType.ONIX_TWO_SHORT),
            any()
        )
    }

    @Test
    @Ignore
    fun testRunOnixThreeLong() = runBlocking {
    }

    @Test
    @Ignore
    fun testRunOnixThreeShort() = runBlocking {
    }
}