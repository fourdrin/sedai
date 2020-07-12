package app.fourdrin.sedai.models.onix.parser

import app.fourdrin.sedai.models.onix.OnixDocument
import app.fourdrin.sedai.models.onix.v2.ONIXMessageLong
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream

class TwoLongOnixParserStrategy : OnixParserStrategy() {
    override fun parseMetadataFile(metadataFile: InputStream): OnixDocument {
        var doc: ONIXMessageLong = ONIXMessageLong()
        doc = objectMapper.readValue<ONIXMessageLong>(metadataFile)
        metadataFile.close()
        return doc
    }
}
