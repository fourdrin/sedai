package app.fourdrin.sedai.models.onix.parser

import app.fourdrin.sedai.models.MetadataDocument
import app.fourdrin.sedai.models.onix.v2.MessageV2
import java.io.InputStream

class TwoShortOnixParserStrategy : OnixParserStrategy<MessageV2>() {
    override fun parseMetadataFile(metadataFile: InputStream): MessageV2 {
        TODO()
    }
}
