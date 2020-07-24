package app.fourdrin.sedai.models.onix.parser

import app.fourdrin.sedai.models.metadata.MetadataDocument
import java.io.InputStream

class ThreeShortOnixParserStrategy: OnixParserStrategy<MetadataDocument>() {
    override fun parseMetadataFile(metadataFile: InputStream): MetadataDocument {
        TODO()
    }
}
