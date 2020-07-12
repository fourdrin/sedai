package app.fourdrin.sedai.models.onix.v2

import app.fourdrin.sedai.models.onix.OnixDocument

abstract class OnixDocumentV2 : OnixDocument {
    abstract val header: Header
    abstract val products: List<Product>
}

abstract class Header {
    abstract val fromEANNumber: String?
    abstract val fromSAN: String?
}

abstract class Product {
    abstract val recordReference: String?
    abstract val productIdentifiers: List<ProductIdentifier>
}

abstract class ProductIdentifier {
    abstract val productIDType: String?
    abstract val idTypeName: String?
    abstract val idValue: String?
}
