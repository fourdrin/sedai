package worker.ftp.tasks

import app.fourdrin.sedai.SEDAI_FTP_ROOT_DIRECTORY
import app.fourdrin.sedai.ftp.tasks.CheckpointRunnable
import app.fourdrin.sedai.models.ftp.Manifest
import app.fourdrin.sedai.models.worker.AssetType
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*

internal class CheckpointRunnableTest {
    private val accountsRequest = ListObjectsV2Request.builder()
        .bucket(SEDAI_FTP_ROOT_DIRECTORY)
        .delimiter("/")
        .build()

    private val accountsResponse = ListObjectsV2Response.builder()
        .commonPrefixes(CommonPrefix.builder().prefix("internal/").build())
        .build()

    private val accountRequest = ListObjectsV2Request.builder()
        .bucket(SEDAI_FTP_ROOT_DIRECTORY)
        .prefix("internal")
        .continuationToken(null)
        .build()

    private val s3Objects = listOf<S3Object>(
        S3Object.builder().key("internal/foo.xml").build(),
        S3Object.builder().key("internal/test.epub").build(),
        S3Object.builder().key("internal/test.jpg").build()
    )

    private val s3ObjectsAlt = listOf<S3Object>(
        S3Object.builder().key("internal/foo.xml").build(),
        S3Object.builder().key("internal/test.epub").build(),
        S3Object.builder().key("internal/test.jpeg").build()
    )

    private val accountResponse = ListObjectsV2Response.builder()
        .continuationToken(null)
        .contents(s3Objects)
        .build()

    private val accountResponseAlt = ListObjectsV2Response.builder()
        .continuationToken(null)
        .contents(s3ObjectsAlt)
        .build()

    val requestBodyArgCaptor = ArgumentCaptor.forClass(RequestBody::class.java)

    @Test
    fun testGeneratedManifestFile() {
        val s3Client: S3Client = mock<S3Client>() {
            onGeneric {
                listObjectsV2(accountsRequest)
            } doReturn accountsResponse

            onGeneric {
                listObjectsV2(accountRequest)
            } doReturn accountResponse
        }

        CheckpointRunnable(s3Client).run()

        verify(s3Client).putObject(any<PutObjectRequest>(), requestBodyArgCaptor.capture())

        val manifestFile = String(requestBodyArgCaptor.value.contentStreamProvider().newStream().readBytes())
        val manifestJSON = Gson().fromJson(manifestFile, Manifest::class.java)

        // id
        assertNotNull(manifestJSON.id)

        // account
        assertEquals(1, manifestJSON.accounts.keys.size)
        assertTrue(manifestJSON.accounts.containsKey("internal"))

        val account = manifestJSON.accounts["internal"]

        assertEquals("internal", account?.name)

        // metadata files
        assertTrue(account?.metadataFiles?.contains("internal/foo.xml") ?: false)

        // asset files
        val epub = account?.assetFiles?.get("test")?.get(AssetType.EPUB)
        assertEquals("internal/test.epub", epub)

        val cover = account?.assetFiles?.get("test")?.get(AssetType.COVER)
        assertEquals("internal/test.jpg", cover)
    }

    @Test
    fun testGeneratedManifestFileJpegCover() {
        val s3Client: S3Client = mock<S3Client>() {
            onGeneric {
                listObjectsV2(accountsRequest)
            } doReturn accountsResponse

            onGeneric {
                listObjectsV2(accountRequest)
            } doReturn accountResponseAlt
        }

        CheckpointRunnable(s3Client).run()
        verify(s3Client).putObject(any<PutObjectRequest>(), requestBodyArgCaptor.capture())

        val manifestFile = String(requestBodyArgCaptor.value.contentStreamProvider().newStream().readBytes())
        val manifestJSON = Gson().fromJson(manifestFile, Manifest::class.java)
        val account = manifestJSON.accounts["internal"]

        val cover = account?.assetFiles?.get("test")?.get(AssetType.COVER)
        assertEquals("internal/test.jpeg", cover)
    }
}