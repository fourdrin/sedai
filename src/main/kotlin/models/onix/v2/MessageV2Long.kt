import app.fourdrin.sedai.models.onix.TagV2
import app.fourdrin.sedai.models.onix.v2.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "ONIXMessage")
data class MessageV2Long(
    @JacksonXmlProperty(localName = TagV2.Header.LONG)
    override val header: HeaderLong,

    @JacksonXmlProperty(localName = TagV2.Product.LONG)
    override val products: List<ProductLong>
) : MessageV2

// Header

@JsonIgnoreProperties(ignoreUnknown = true)
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

@JsonIgnoreProperties(ignoreUnknown = true)
data class SenderIdentifierLong(
    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.SenderIDType.LONG)
    override val senderIDType: String,

    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.IDTypeName.LONG)
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.IDValue.LONG)
    override val idValue: String?
) : SenderIdentifier

@JsonIgnoreProperties(ignoreUnknown = true)
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
    @JacksonXmlProperty(localName = "RecordReference")
    override val recordReference: String?,

    @JacksonXmlProperty(localName = "NotificationType")
    override val notificationType: String?,

    @JacksonXmlProperty(localName = "ProductIdentifier")
    override val productIdentifiers: List<ProductIdentifierLong>,

    @JacksonXmlProperty(localName = "ProductForm")
    override val productForm: String?

) : Product

data class ProductIdentifierLong(
    @JacksonXmlProperty(localName = "IDTypeName")
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = "IDValue")
    override val idValue: String?,

    @JacksonXmlProperty(localName = "ProductIDType")
    override val productIDType: String?
) : ProductIdentifier
