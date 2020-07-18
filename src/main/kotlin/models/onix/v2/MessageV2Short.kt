package app.fourdrin.sedai.models.onix.v2

import app.fourdrin.sedai.models.onix.TagV2
import app.fourdrin.sedai.models.onix.v2.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = TagV2.Message.SHORT)
data class MessageV2Short(
    @JacksonXmlProperty(localName = TagV2.Header.SHORT)
    override val header: HeaderShort,

    @JacksonXmlProperty(localName = TagV2.Product.SHORT)
    override val products: List<ProductShort>
) : MessageV2

// Header

data class HeaderShort(
    @JacksonXmlProperty(localName = TagV2.Header.FromSAN.SHORT)
    override val fromSAN: String? = "",

    @JacksonXmlProperty(localName = TagV2.Header.FromEANNumber.SHORT)
    override val fromEANNumber: String? = "",

    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.SHORT)
    override val senderIdentifiers: List<SenderIdentifierShort>? = listOf(),

    @JacksonXmlProperty(localName = TagV2.Header.FromCompany.SHORT)
    override val fromCompany: String?,

    @JacksonXmlProperty(localName = TagV2.Header.FromPerson.SHORT)
    override val fromPerson: String?,

    @JacksonXmlProperty(localName = TagV2.Header.FromEmail.SHORT)
    override val fromEmail: String?,

    @JacksonXmlProperty(localName = TagV2.Header.ToEANNumber.SHORT)
    override val toEANNumber: String?,

    @JacksonXmlProperty(localName = TagV2.Header.ToSAN.SHORT)
    override val toSAN: String?,

    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.SHORT)
    override val addresseeIdentifiers: List<AddresseeIdentifierShort>?,

    @JacksonXmlProperty(localName = TagV2.Header.ToCompany.SHORT)
    override val toCompany: String?,

    @JacksonXmlProperty(localName = TagV2.Header.ToPerson.SHORT)
    override val toPerson: String?,

    @JacksonXmlProperty(localName = TagV2.Header.MessageNumber.SHORT)
    override val messageNumber: String?,

    @JacksonXmlProperty(localName = TagV2.Header.MessageRepeat.SHORT)
    override val messageRepeat: String?,

    @JacksonXmlProperty(localName = TagV2.Header.SentDate.SHORT)
    override val sentDate: String,

    @JacksonXmlProperty(localName = TagV2.Header.MessageNote.SHORT)
    override val messageNote: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultLanguageOfText.SHORT)
    override val defaultLanguageOfText: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultPriceTypeCode.SHORT)
    override val defaultPriceTypeCode: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultCurrencyCode.SHORT)
    override val defaultCurrencyCode: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultLinearUnit.SHORT)
    override val defaultLinearUnit: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultWeightUnit.SHORT)
    override val defaultWeightUnit: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultClassOfTrade.SHORT)
    override val defaultClassOfTrade: String?
) : Header

data class SenderIdentifierShort(
    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.SenderIDType.SHORT)
    override val senderIDType: String,

    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.IDTypeName.SHORT)
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.IDValue.SHORT)
    override val idValue: String?
) : SenderIdentifier

@JsonIgnoreProperties(ignoreUnknown = true)
data class AddresseeIdentifierShort(
    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.AddresseeIDType.SHORT)
    override val addresseeIDType: String,

    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.IDTypeName.SHORT)
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.IDValue.SHORT)
    override val idValue: String?

) : AddresseeIdentifier

// Products

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProductShort(
    @JacksonXmlProperty(localName = "RecordReference")
    override val recordReference: String?,

    @JacksonXmlProperty(localName = "NotificationType")
    override val notificationType: String?,

    @JacksonXmlProperty(localName = "ProductIdentifier")
    override val productIdentifiers: List<ProductIdentifierShort>,

    @JacksonXmlProperty(localName = "ProductForm")
    override val productForm: String?

) : Product

data class ProductIdentifierShort(
    @JacksonXmlProperty(localName = "IDTypeName")
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = "IDValue")
    override val idValue: String?,

    @JacksonXmlProperty(localName = "ProductIDType")
    override val productIDType: String?
) : ProductIdentifier
