package app.fourdrin.sedai.onix

interface ParserStrategy {
    fun parseMetadataFile(metadataFileKey: String)
}