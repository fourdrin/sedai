package worker.ftp.tasks

import app.fourdrin.sedai.SEDAI_FTP_ROOT_DIRECTORY
import app.fourdrin.sedai.SEDAI_MANIFEST_NAME
import app.fourdrin.sedai.SEDAI_PIPELINE_DIRECTORY
import app.fourdrin.sedai.worker.ftp.tasks.FileSyncRunnable
import app.fourdrin.sedai.worker.loader.LoaderClient
import app.fourdrin.sedai.models.ftp.Account
import app.fourdrin.sedai.models.ftp.Manifest
import app.fourdrin.sedai.models.worker.AssetType
import app.fourdrin.sedai.models.worker.FTPWork
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.*
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.testing.GrpcCleanupRule
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import software.amazon.awssdk.core.ResponseBytes
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.time.Instant

internal class FileSyncRunnableTest {
    private val now = Instant.now()
    private val internalAccount = Account(
        name = "test",
        metadataFiles = listOf("test/foo.xml"),
        assetFiles = mapOf(
            "test" to mapOf<AssetType, String>(
                AssetType.EPUB to "test/test.epub",
                AssetType.COVER to "test/test.jpg"
            )
        )
    )
    private val id = now.epochSecond.toString()
    private val manifest = Manifest(
        id = id,
        startedAt = now.toString(),
        accounts = mapOf(
            "test" to internalAccount
        )
    )

    private val manifestJSON = Gson().toJson(manifest).toByteArray()

    private val manifestRequest = GetObjectRequest.builder()
        .bucket(SEDAI_PIPELINE_DIRECTORY)
        .key("$id/$SEDAI_MANIFEST_NAME")
        .build()

    private val manifestResponse = GetObjectResponse.builder()
        .contentType("application/json")
        .build()

    private val s3Client: S3Client = mock<S3Client>() {
        on {
            getObject(manifestRequest, ResponseTransformer.toBytes())
        } doReturn ResponseBytes.fromByteArray(manifestResponse, manifestJSON)
    }

    private val loaderService = Mockito.mock(
        LoaderServiceGrpcKt.LoaderServiceCoroutineImplBase::class.java,
        AdditionalAnswers.delegatesTo<LoaderServiceGrpcKt.LoaderServiceCoroutineImplBase>(
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


    private val loaderClient = Mockito.mock(LoaderClient::class.java)

    private val work = FTPWork(
        id = id,
        accountName = "test",
        manifestName = SEDAI_MANIFEST_NAME
    )

    private val copyObjectCaptor = ArgumentCaptor.forClass(CopyObjectRequest::class.java)
    private val deleteObjectCaptor = ArgumentCaptor.forClass(DeleteObjectRequest::class.java)

    @Test
    fun testRunMetadata() = runBlocking {
        FileSyncRunnable(s3Client, loaderClient, work).run()

        // Verify the objects were copied
        verify(s3Client).copyObject(copyObjectCaptor.capture())
        assertEquals("books-worker.ftp/test/foo.xml", copyObjectCaptor.value.copySource())
        assertEquals(SEDAI_PIPELINE_DIRECTORY, copyObjectCaptor.value.destinationBucket())
        assertEquals("$id/test/foo.xml", copyObjectCaptor.value.destinationKey())

        // Verify the objects were deleted
        verify(s3Client).deleteObject(deleteObjectCaptor.capture())
        assertEquals(SEDAI_FTP_ROOT_DIRECTORY, deleteObjectCaptor.value.bucket())
        assertEquals("test/foo.xml", deleteObjectCaptor.value.key())

        // Verify the work was queued via a gRPC request
        verify(loaderClient).createLoad(
            eq("$id/test/foo.xml"),
            eq(LoaderServiceOuterClass.AssetType.METADATA),
            eq(LoaderServiceOuterClass.MetadataType.UNKNOWN),
            any()
        )
    }
}