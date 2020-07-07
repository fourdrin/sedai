package app.fourdrin.sedai.loader.tasks

import app.fourdrin.sedai.SEDAI_PIPELINE_DIRECTORY
import app.fourdrin.sedai.loader.LoaderWorkerWithQueue
import app.fourdrin.sedai.models.AssetType
import app.fourdrin.sedai.models.LoaderWork
import app.fourdrin.sedai.models.MetadataVersion
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamException

private sealed class OnixFile

private object ThreeLong : OnixFile()
private object ThreeShort : OnixFile()
private object TwoLong : OnixFile()
private object TwoShort : OnixFile()

class MetadataRunnable constructor(private val s3Client: S3Client, private val metadataKey: String) : Runnable {
    private val inputFactory = XMLInputFactory.newInstance()

    init {
        inputFactory.setProperty(
            XMLInputFactory.SUPPORT_DTD, false
        )
    }

    override fun run() {
        // Open the ONIX file
        val request = GetObjectRequest.builder()
            .bucket(SEDAI_PIPELINE_DIRECTORY)
            .key(metadataKey)
            .build()

        val resp = s3Client.getObject(request, ResponseTransformer.toInputStream())

        // Figure out the first tag in the ONIX file.  We'll use that to determine _which_ type of ONIX file we are working with
        var firstTag: String? = null

        try {
            val xmlReader = inputFactory.createXMLStreamReader(resp)
            while (xmlReader.hasNext()) {
                if (xmlReader.next() == 1) {
                    firstTag = xmlReader.localName
                    break
                }
            }
        } catch (e: IllegalStateException) {
            println(e)
        } catch (e: XMLStreamException) {
            println(e)
        }

        if (firstTag != null) {
            val metadataVersion = when (firstTag) {
                "ONIXMessageAcknowledgement" -> MetadataVersion.THREE_LONG
                "ONIXmessageacknowledgement" -> MetadataVersion.THREE_SHORT
                "ONIXMessage" -> MetadataVersion.TWO_LONG
                "ONIXmessage" -> MetadataVersion.TWO_SHORT
                else -> MetadataVersion.UNKNOWN
            }


            if (metadataVersion != MetadataVersion.UNKNOWN) {
                val work = LoaderWork(
                    id = metadataKey,
                    assetType = AssetType.METADATA,
                    metadataVersion = metadataVersion
                )

                // Requeue the work now that we know the version
                LoaderWorkerWithQueue.workerQueue.add(work)
                return
            }
        }

        println("Unable to determine metadata version")
    }
}
