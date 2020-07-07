package app.fourdrin.sedai.models


enum class AssetType {
    METADATA,
    EPUB,
    COVER,
}

enum class MetadataVersion {
    TWO_SHORT,
    TWO_LONG,
    THREE_SHORT,
    THREE_LONG,
    UNKNOWN
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
    val metadataVersion: MetadataVersion = MetadataVersion.UNKNOWN
) : Work()