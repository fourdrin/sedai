package app.fourdrin.sedai.models.metadata

import java.io.InputStream

interface MetadataParserStrategy<out T: MetadataDocument> {
    fun parseMetadataFile(metadataFile: InputStream): T
}