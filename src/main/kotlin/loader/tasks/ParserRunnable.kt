package app.fourdrin.sedai.loader.tasks

import app.fourdrin.sedai.onix.ParserStrategy

class ParserRunnable constructor(private val strategy: ParserStrategy, private val metadataFileKey: String) : Runnable {
    override fun run() {
        strategy.parseMetadataFile(metadataFileKey)
    }
}