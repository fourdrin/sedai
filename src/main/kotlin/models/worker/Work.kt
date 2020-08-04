package app.fourdrin.sedai.models.worker

import app.fourdrin.sedai.models.metadata.MetadataType
import java.io.InputStream


enum class FileType {
    METADATA,
    EPUB,
    COVER,
}


sealed class Work {
    abstract val id: String
}

data class FTPWork(
    override val id: String,
    val accountName: String,
    val manifestKey: String
) : Work()

data class LoaderWork(
    override val id: String,
    val fileType: FileType,
    val metadataType: MetadataType? = null,
    val metadataFile: InputStream? = null
) : Work()
