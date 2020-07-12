package app.fourdrin.sedai.models

import app.fourdrin.sedai.models.onix.MetadataVersion
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
    val metadataVersion: MetadataVersion,
    val metadataFile: InputStream? = null
) : Work()