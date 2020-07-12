package app.fourdrin.sedai.models.onix.parser

import app.fourdrin.sedai.models.onix.OnixDocument
import app.fourdrin.sedai.models.onix.ThreeShort
import app.fourdrin.sedai.models.onix.parser.OnixParserStrategy
import java.io.InputStream

class ThreeShortOnixParserStrategy: OnixParserStrategy() {
    override fun parseMetadataFile(metadataFile: InputStream): OnixDocument {
        TODO()
    }
}
