package app.fourdrin.sedai.models.onix.v2

import app.fourdrin.sedai.models.MetadataDocument


interface MessageV2 : MetadataDocument {
    override val header: Header?
    override val products: List<Product>
}

interface Header {
    val fromEANNumber: String?
    val fromSAN: String?
}

interface Product {
    val recordReference: String?
    val notificationType: String?
    val productForm: String?
    val productIdentifiers: List<ProductIdentifier>
}

interface ProductIdentifier {
    val productIDType: String?
    val idTypeName: String?
    val idValue: String?
}
