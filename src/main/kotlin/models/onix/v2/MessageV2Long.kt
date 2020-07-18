
import app.fourdrin.sedai.models.onix.v2.Header
import app.fourdrin.sedai.models.onix.v2.MessageV2
import app.fourdrin.sedai.models.onix.v2.Product
import app.fourdrin.sedai.models.onix.v2.ProductIdentifier
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "ONIXMessage")
data class MessageV2Long (
    @JacksonXmlProperty(localName = "Header")
    override val header: HeaderLong?,

    @JacksonXmlProperty(localName = "Product")
    override val products: List<ProductLong>
) : MessageV2

@JsonIgnoreProperties(ignoreUnknown = true)
data class HeaderLong (
    @JacksonXmlProperty(localName = "FromSAN")
    override val fromSAN: String? = "",

    @JacksonXmlProperty(localName = "FromEANNumber")
    override val fromEANNumber: String? = ""
) : Header

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
): ProductIdentifier
