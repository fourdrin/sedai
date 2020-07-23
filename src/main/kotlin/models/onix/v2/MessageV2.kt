package app.fourdrin.sedai.models.onix.v2

import app.fourdrin.sedai.models.MetadataDocument


interface MessageV2 : MetadataDocument {
    override val header: Header
    override val products: List<Product>
}

interface IdentifierComposite {
    val idTypeName: String?
    val idValue: String?
}

// Header

interface Header {
    val fromEANNumber: String?
    val fromSAN: String?
    val senderIdentifiers: List<SenderIdentifier>?
    val fromCompany: String?
    val fromPerson: String?
    val fromEmail: String?
    val toEANNumber: String?
    val toSAN: String?
    val addresseeIdentifiers: List<AddresseeIdentifier>?
    val toCompany: String?
    val toPerson: String?
    val messageNumber: String?
    val messageRepeat: String?
    val sentDate: String
    val messageNote: String?
    val defaultLanguageOfText: String?
    val defaultPriceTypeCode: String?
    val defaultCurrencyCode: String?
    @Deprecated("No longer supported in ONIX 2.1") val defaultLinearUnit: String?
    @Deprecated("No longer supported in ONIX 2.1") val defaultWeightUnit: String?
    val defaultClassOfTrade: String?
}

interface SenderIdentifier : IdentifierComposite {
    val senderIDType: String
}

interface AddresseeIdentifier : IdentifierComposite {
    val addresseeIDType: String
}

// Products

interface Product : ProductIdentifiers, ProductForm {
    val recordReference: String
    val notificationType: String
    val deletionCode: String?
    val deletionText: String?

    val recordSourceType: String?
    val recordSourceIdentifierType: String?
    val recordSourceIdentifier: String?
    val recordSourceName: String?

    @Deprecated("No longer supported in ONIX 2.1") val upc: String?
    @Deprecated("No longer supported in ONIX 2.1") val publisherProductNo: String?
    @Deprecated("No longer supported in ONIX 2.1") val ismn: String?
    @Deprecated("No longer supported in ONIX 2.1") val doi: String?
    val barcodes: List<String>?
    @Deprecated("No longer supported in ONIX 2.1") val replacesISBN: String?
    @Deprecated("No longer supported in ONIX 2.1") val replacesEAN13: String?

    val containedItems: List<ContainedItem>?

    val productClassifications: List<ProductClassification>?
}

interface ProductForm {
    val productForm: String?
    val productFormDetails: List<String>?
    val productFormFeatures: List<ProductFormFeature>?

    @Deprecated("No longer supported in ONIX 2.1") val bookFormDetail: String?
    val productPackaging: String?
    val productFormDescription: String?
    val numberOfPieces: Int?
    val tradeCategory: String?
    val productContentTypes: List<String>?
}

interface ProductIdentifiers {
    @Deprecated("No longer supported in ONIX 2.1") val isbn: String?
    @Deprecated("No longer supported in ONIX 2.1") val ean13: String?
    val productIdentifiers: List<ProductIdentifier>
}

interface ProductIdentifier : IdentifierComposite {
    val productIDType: String?
}

interface ProductFormFeature {
    val productFormFeatureType: String?
    val productFormFeatureValue: String?
    val productFormFeatureDescription: String?
}

interface ContainedItem : ProductIdentifiers, ProductForm {
    val itemsQuantity: Int?
}

interface ProductClassification {
    val productClassificationType: String
    val productClassificationCode: String
    val percent: String?
}
