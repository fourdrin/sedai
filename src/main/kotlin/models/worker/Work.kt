package app.fourdrin.sedai.models.worker

import app.fourdrin.sedai.models.metadata.MetadataType
import java.io.InputStream


enum class AssetType {
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
    val manifestName: String
) : Work() {
    val accountS3Key: String
        get() = "${this.id}/${this.accountName}/"

    val manifestS3Key: String
        get() = "${this.id}/${this.manifestName}"
}

data class LoaderWork(
    override val id: String,
    val assetType: AssetType,
    val metadataType: MetadataType,
    val metadataFile: InputStream? = null
) : Work()