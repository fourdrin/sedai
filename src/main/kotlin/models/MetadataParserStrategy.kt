package app.fourdrin.sedai.models

import app.fourdrin.sedai.models.MetadataDocument
import app.fourdrin.sedai.models.onix.OnixDocument
import java.io.InputStream

interface MetadataParserStrategy {
    fun parseMetadataFile(metadataFile: InputStream): MetadataDocument
}