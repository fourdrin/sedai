package app.fourdrin.sedai.models

data class Work(
    val id: String,
    val accountName: String,
    val manifestName: String
) {
    val accountS3Key: String
        get() = "${this.id}/${this.accountName}/"

    val manifestS3Key: String
        get() = "${this.id}/${this.manifestName}"
}