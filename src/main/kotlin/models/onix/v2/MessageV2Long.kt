import app.fourdrin.sedai.models.onix.TagV2
import app.fourdrin.sedai.models.onix.v2.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import javax.swing.text.html.HTML

@JacksonXmlRootElement(localName = TagV2.Message.LONG)
data class MessageV2Long(
    @JacksonXmlProperty(localName = TagV2.Header.LONG)
    override val header: HeaderLong,

    @JacksonXmlProperty(localName = TagV2.Product.LONG)
    override val products: List<ProductLong>
) : MessageV2

// Header

data class HeaderLong(
    @JacksonXmlProperty(localName = TagV2.Header.FromSAN.LONG)
    override val fromSAN: String? = "",

    @JacksonXmlProperty(localName = TagV2.Header.FromEANNumber.LONG)
    override val fromEANNumber: String? = "",

    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.LONG)
    override val senderIdentifiers: List<SenderIdentifierLong>? = listOf(),

    @JacksonXmlProperty(localName = TagV2.Header.FromCompany.LONG)
    override val fromCompany: String?,

    @JacksonXmlProperty(localName = TagV2.Header.FromPerson.LONG)
    override val fromPerson: String?,

    @JacksonXmlProperty(localName = TagV2.Header.FromEmail.LONG)
    override val fromEmail: String?,

    @JacksonXmlProperty(localName = TagV2.Header.ToEANNumber.LONG)
    override val toEANNumber: String?,

    @JacksonXmlProperty(localName = TagV2.Header.ToSAN.LONG)
    override val toSAN: String?,

    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.LONG)
    override val addresseeIdentifiers: List<AddresseeIdentifierLong>?,

    @JacksonXmlProperty(localName = TagV2.Header.ToCompany.LONG)
    override val toCompany: String?,

    @JacksonXmlProperty(localName = TagV2.Header.ToPerson.LONG)
    override val toPerson: String?,

    @JacksonXmlProperty(localName = TagV2.Header.MessageNumber.LONG)
    override val messageNumber: String?,

    @JacksonXmlProperty(localName = TagV2.Header.MessageRepeat.LONG)
    override val messageRepeat: String?,

    @JacksonXmlProperty(localName = TagV2.Header.SentDate.LONG)
    override val sentDate: String,

    @JacksonXmlProperty(localName = TagV2.Header.MessageNote.LONG)
    override val messageNote: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultLanguageOfText.LONG)
    override val defaultLanguageOfText: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultPriceTypeCode.LONG)
    override val defaultPriceTypeCode: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultCurrencyCode.LONG)
    override val defaultCurrencyCode: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultLinearUnit.LONG)
    override val defaultLinearUnit: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultWeightUnit.LONG)
    override val defaultWeightUnit: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultClassOfTrade.LONG)
    override val defaultClassOfTrade: String?
) : Header

data class SenderIdentifierLong(
    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.SenderIDType.LONG)
    override val senderIDType: String,

    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.IDTypeName.LONG)
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.IDValue.LONG)
    override val idValue: String?
) : SenderIdentifier

data class AddresseeIdentifierLong(
    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.AddresseeIDType.LONG)
    override val addresseeIDType: String,

    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.IDTypeName.LONG)
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.IDValue.LONG)
    override val idValue: String?

) : AddresseeIdentifier

// Products

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProductLong(
    @JacksonXmlProperty(localName = TagV2.Product.RecordReference.LONG)
    override val recordReference: String,

    @JacksonXmlProperty(localName = TagV2.Product.NotificationType.LONG)
    override val notificationType: String,

    @JacksonXmlProperty(localName = TagV2.Product.DeletionCode.LONG)
    override val deletionCode: String?,

    @JacksonXmlProperty(localName = TagV2.Product.DeletionText.LONG)
    override val deletionText: String?,

    @JacksonXmlProperty(localName = TagV2.Product.RecordSourceType.LONG)
    override val recordSourceType: String?,

    @JacksonXmlProperty(localName = TagV2.Product.RecordSourceIdentifierType.LONG)
    override val recordSourceIdentifierType: String?,

    @JacksonXmlProperty(localName = TagV2.Product.RecordSourceIdentifier.LONG)
    override val recordSourceIdentifier: String?,

    @JacksonXmlProperty(localName = TagV2.Product.RecordSourceName.LONG)
    override val recordSourceName: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ISBN.LONG)
    override val isbn: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EAN13.LONG)
    override val ean13: String?,

    @JacksonXmlProperty(localName = TagV2.Product.UPC.LONG)
    override val upc: String?,

    @JacksonXmlProperty(localName = TagV2.Product.PublisherProductNo.LONG)
    override val publisherProductNo: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ISMN.LONG)
    override val ismn: String?,

    @JacksonXmlProperty(localName = TagV2.Product.DOI.LONG)
    override val doi: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductIdentifier.LONG)
    override val productIdentifiers: List<ProductIdentifierLong>,

    @JacksonXmlProperty(localName = TagV2.Product.BarCode.LONG)
    override val barcodes: List<String>?,

    @JacksonXmlProperty(localName = TagV2.Product.ReplacesISBN.LONG)
    override var replacesISBN: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ReplacesEAN13.LONG)
    override var replacesEAN13: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductForm.LONG)
    override val productForm: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormDetail.LONG)
    override val productFormDetails: List<String>?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormFeature.LONG)
    override val productFormFeatures: List<ProductFormFeatureLong>?,

    @JacksonXmlProperty(localName = TagV2.Product.BookFormDetail.LONG)
    override val bookFormDetail: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductPackaging.LONG)
    override val productPackaging: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormDescription.LONG)
    override val productFormDescription: String?,

    @JacksonXmlProperty(localName = TagV2.Product.NumberOfPieces.LONG)
    override val numberOfPieces: Int?,

    @JacksonXmlProperty(localName = TagV2.Product.TradeCategory.LONG)
    override val tradeCategory: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductContentType.LONG)
    override val productContentTypes: List<String>?
) : Product

data class ProductIdentifierLong(
    @JacksonXmlProperty(localName = TagV2.Product.ProductIdentifier.ProductIDType.LONG)
    override val productIDType: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductIdentifier.IDTypeName.LONG)
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductIdentifier.IDValue.LONG)
    override val idValue: String?
) : ProductIdentifier

data class ProductFormFeatureLong(
    @JacksonXmlProperty(localName = TagV2.Product.ProductFormFeature.ProductFormFeatureType.LONG)
    override val productFormFeatureType: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormFeature.ProductFormFeatureValue.LONG)
    override val productFormFeatureValue: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormFeature.ProductFormFeatureDescription.LONG)
    override val productFormFeatureDescription: String?
) : ProductFormFeature
