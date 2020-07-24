package app.fourdrin.sedai.models.onix.v2

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

data class ProductTitle(
    @JacksonXmlProperty(isAttribute = true, localName = "textformat")
    override val textFormat: String = "",

    @JacksonXmlProperty(isAttribute = true, localName = "language")
    override val language: String = "",

    @JacksonXmlProperty(isAttribute = true, localName = "textcase")
    override val textCase: String = "",

    @JacksonXmlProperty(isAttribute = true, localName = "transliteration")
    override val transliteration: String = ""
) : TitleText {
    @JacksonXmlText
    override val value: String = ""
}
