package app.fourdrin.sedai.models

data class Manifest(val id: String, val startedAt: String, val accounts: Map<String, Account>)
data class Account(val name: String, val metadataFiles: List<String?> = listOf(), val assetFiles: Map<String, Asset> = hashMapOf())

typealias Asset = Map<AssetType, String?>
