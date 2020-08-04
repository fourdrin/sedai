package worker.ftp

import app.fourdrin.sedai.SEDAI_FTP_ROOT_DIRECTORY
import app.fourdrin.sedai.SEDAI_MANIFEST_NAME
import app.fourdrin.sedai.SEDAI_PIPELINE_DIRECTORY
import app.fourdrin.sedai.worker.ftp.FileSyncRunnable
import app.fourdrin.sedai.grpc.LoaderClient
import app.fourdrin.sedai.models.ftp.Account
import app.fourdrin.sedai.models.ftp.Manifest
import app.fourdrin.sedai.models.worker.FileType
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
            "test" to mapOf<FileType, String>(
                FileType.EPUB to "test/test.epub",
                FileType.COVER to "test/test.jpg"
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
                override suspend fun createMetadataJob(request: LoaderServiceOuterClass.CreateMetadataJobRequest): LoaderServiceOuterClass.CreateMetadataJobResponse {
                    return LoaderServiceOuterClass.CreateMetadataJobResponse.newBuilder().setQueued(true).build()
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
        manifestKey = "$id/$SEDAI_MANIFEST_NAME"
    )

    private val copyObjectCaptor = ArgumentCaptor.forClass(CopyObjectRequest::class.java)
    private val deleteObjectCaptor = ArgumentCaptor.forClass(DeleteObjectRequest::class.java)

    @Test
    fun testRun() {
        FileSyncRunnable(s3Client, loaderClient, work).run()

        // Verify the objects were copied and deleted
        verify(s3Client, times(3)).copyObject(copyObjectCaptor.capture())
        verify(s3Client, times(3)).deleteObject(deleteObjectCaptor.capture())

        // Metadata
        assertEquals("books-worker.ftp/test/foo.xml", copyObjectCaptor.allValues[0].copySource())
        assertEquals(SEDAI_PIPELINE_DIRECTORY, copyObjectCaptor.allValues[0].destinationBucket())
        assertEquals("$id/test/foo.xml", copyObjectCaptor.allValues[0].destinationKey())

        assertEquals(SEDAI_FTP_ROOT_DIRECTORY, deleteObjectCaptor.allValues[0].bucket())
        assertEquals("test/foo.xml", deleteObjectCaptor.allValues[0].key())

        // EPUBs
        assertEquals("books-worker.ftp/test/test.epub", copyObjectCaptor.allValues[1].copySource())
        assertEquals(SEDAI_PIPELINE_DIRECTORY, copyObjectCaptor.allValues[1].destinationBucket())
        assertEquals("$id/test/test.epub", copyObjectCaptor.allValues[1].destinationKey())

        assertEquals(SEDAI_FTP_ROOT_DIRECTORY, deleteObjectCaptor.allValues[1].bucket())
        assertEquals("test/test.epub", deleteObjectCaptor.allValues[1].key())

        // Covers
        assertEquals("books-worker.ftp/test/test.jpg", copyObjectCaptor.allValues[2].copySource())
        assertEquals(SEDAI_PIPELINE_DIRECTORY, copyObjectCaptor.allValues[2].destinationBucket())
        assertEquals("$id/test/test.jpg", copyObjectCaptor.allValues[2].destinationKey())

        assertEquals(SEDAI_FTP_ROOT_DIRECTORY, deleteObjectCaptor.allValues[2].bucket())
        assertEquals("test/test.jpg", deleteObjectCaptor.allValues[2].key())


        // Verify all the work was queued via a gRPC request
        runBlocking {
            verify(loaderClient).createMetadataJob(
                eq("$id/test/foo.xml"),
                eq(LoaderServiceOuterClass.MetadataType.UNKNOWN),
                any()
            )

            verify(loaderClient).createEpubJob(
                eq("$id/test/test.epub"),
                any()
            )

            verify(loaderClient).createCoverJob(
                eq("$id/test/test.jpg"),
                any()
            )
        }
    }
}