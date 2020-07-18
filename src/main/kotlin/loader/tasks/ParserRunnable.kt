package app.fourdrin.sedai.loader.tasks

import app.fourdrin.sedai.models.MetadataDocument
import app.fourdrin.sedai.models.MetadataParserStrategy
import java.io.InputStream

class ParserRunnable constructor(private val metadataStrategy: MetadataParserStrategy<MetadataDocument>, private val metadataFile: InputStream?) : Runnable {
    override fun run() {
        if (metadataFile != null) {
            println("Parsing: $metadataStrategy")

            val d = metadataStrategy.parseMetadataFile(metadataFile)
            println(d)
        }
    }
}