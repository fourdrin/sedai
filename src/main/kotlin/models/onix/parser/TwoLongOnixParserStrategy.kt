package app.fourdrin.sedai.models.onix.parser

import MessageV2Long
import app.fourdrin.sedai.models.onix.v2.MessageV2
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream

class TwoLongOnixParserStrategy : OnixParserStrategy<MessageV2>() {
    override fun parseMetadataFile(metadataFile: InputStream): MessageV2 {
        val doc = objectMapper.readValue<MessageV2Long>(metadataFile)
        metadataFile.close()
        return doc
    }
}
