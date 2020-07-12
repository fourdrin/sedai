package app.fourdrin.sedai.models.onix.v2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "ONIXMessage")
class ONIXMessageLong: OnixDocumentV2() {
    @JacksonXmlProperty(localName = "Header")
    override val header: Header = HeaderLong()

    @JacksonXmlProperty(localName = "Product")
    override val products: List<ProductLong> = listOf()
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class HeaderLong (
    @JacksonXmlProperty(localName = "FromSAN")
    override val fromSAN: String? = "",

    @JacksonXmlProperty(localName = "FromEANNumber")
    override val fromEANNumber: String? = ""
) : Header()

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProductLong(
    @JacksonXmlProperty(localName = "RecordReference")
    override val recordReference: String,

    @JacksonXmlProperty(localName = "ProductIdentifier")
    override val productIdentifiers: List<ProductIdentifierLong>
) : Product()

data class ProductIdentifierLong(
    @JacksonXmlProperty(localName = "IDTypeName")
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = "IDValue")
    override val idValue: String?,

    @JacksonXmlProperty(localName = "ProductIDType")
    override val productIDType: String?
): ProductIdentifier()
