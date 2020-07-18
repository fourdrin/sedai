package app.fourdrin.sedai.models.onix.parser

import app.fourdrin.sedai.models.onix.v2.MessageV2
import app.fourdrin.sedai.models.onix.v2.MessageV2Short
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream

class TwoShortOnixParserStrategy : OnixParserStrategy<MessageV2>() {
    override fun parseMetadataFile(metadataFile: InputStream): MessageV2 {
        val doc = objectMapper.readValue<MessageV2Short>(metadataFile)
        metadataFile.close()
        return doc
    }
}
