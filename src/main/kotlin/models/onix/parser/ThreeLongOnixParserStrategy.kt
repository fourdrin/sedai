package app.fourdrin.sedai.models.onix.parser

import app.fourdrin.sedai.models.metadata.MetadataDocument
import java.io.InputStream

class ThreeLongOnixParserStrategy: OnixParserStrategy<MetadataDocument>() {
    override fun parseMetadataFile(metadataFile: InputStream): MetadataDocument {
        TODO()
    }
}
