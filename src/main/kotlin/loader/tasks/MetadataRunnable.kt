package app.fourdrin.sedai.loader.tasks

import app.fourdrin.sedai.SEDAI_PIPELINE_DIRECTORY
import app.fourdrin.sedai.loader.LoaderWorkerWithQueue
import app.fourdrin.sedai.models.AssetType
import app.fourdrin.sedai.models.LoaderWork
import app.fourdrin.sedai.models.onix.MetadataVersion
import app.fourdrin.sedai.models.onix.ThreeLong
import app.fourdrin.sedai.models.onix.ThreeShort
import app.fourdrin.sedai.models.onix.TwoLong
import app.fourdrin.sedai.models.onix.TwoShort
import app.fourdrin.sedai.models.onix.Unknown
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamException

class MetadataRunnable constructor(private val s3Client: S3Client, private val metadataKey: String) : Runnable {
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
            val metadataVersion: MetadataVersion = when (firstTag) {
                "ONIXMessageAcknowledgement" -> ThreeLong
                "ONIXmessageacknowledgement" -> ThreeShort
                "ONIXMessage" -> TwoLong
                "ONIXmessage" -> TwoShort
                else -> Unknown
            }


            if (metadataVersion != Unknown) {
                val metadataFile = ByteArrayInputStream(resp.asByteArray())
                val work = LoaderWork(
                    id = metadataKey,
                    assetType = AssetType.METADATA,
                    metadataVersion = metadataVersion,
                    metadataFile = metadataFile
                )

                // Requeue the work now that we know the version
                LoaderWorkerWithQueue.workerQueue.add(work)
                return
            }
        }

        println("Unable to determine metadata version")
    }
}
