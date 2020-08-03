package app.fourdrin.sedai.worker.job

import LoaderServiceOuterClass
import app.fourdrin.sedai.SEDAI_PIPELINE_DIRECTORY
import app.fourdrin.sedai.grpc.LoaderClient
import com.google.protobuf.ByteString
import kotlinx.coroutines.runBlocking
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamException

class MetadataRunnable constructor(val s3Client: S3Client, private val metadataKey: String, private val loaderClient: LoaderClient) : Runnable {
    private val inputFactory = XMLInputFactory.newInstance()

    init {
        inputFactory.setProperty(
            XMLInputFactory.SUPPORT_DTD, false
        )
    }

    override fun run() {
        println("Determining metadata version...")
        // Open the ONIX file
        val request = GetObjectRequest.builder()
            .bucket(SEDAI_PIPELINE_DIRECTORY)
            .key(metadataKey)
            .build()

        val resp = s3Client.getObject(request, ResponseTransformer.toBytes())

        // Figure out the first tag in the ONIX file.  We'll use that to determine _which_ type of ONIX file we are working with
        var firstTag: String? = null

        try {
            val doc = ByteArrayInputStream(resp.asByteArray())
            val xmlReader = inputFactory.createXMLStreamReader(doc)
            while (xmlReader.hasNext()) {
                if (xmlReader.next() == 1) {
                    firstTag = xmlReader.localName
                    doc.close()
                    break
                }
            }
        } catch (e: IllegalStateException) {
            println(e)
        } catch (e: XMLStreamException) {
            println(e)
        } catch (e: IOException) {
            println(e)
        }

        if (firstTag != null) {
            val metadataType: LoaderServiceOuterClass.MetadataType = when (firstTag) {
                "ONIXMessageAcknowledgement" -> LoaderServiceOuterClass.MetadataType.ONIX_THREE_LONG
                "ONIXmessageacknowledgement" -> LoaderServiceOuterClass.MetadataType.ONIX_THREE_SHORT
                "ONIXMessage" -> LoaderServiceOuterClass.MetadataType.ONIX_TWO_LONG
                "ONIXmessage" -> LoaderServiceOuterClass.MetadataType.ONIX_TWO_SHORT
                else -> LoaderServiceOuterClass.MetadataType.UNRECOGNIZED
            }


            if (metadataType != LoaderServiceOuterClass.MetadataType.UNRECOGNIZED) {
                val metadataFile = ByteArrayInputStream(resp.asByteArray())

                runBlocking {
                    loaderClient.createLoad(
                        s3Key = metadataKey,
                        assetType = LoaderServiceOuterClass.AssetType.METADATA,
                        metadataType = metadataType,
                        metadataFile = ByteString.copyFrom(resp.asByteArray())
                    )
                }

                return
            }
        }

        println("Unable to determine metadata version")
    }
}
