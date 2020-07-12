package app.fourdrin.sedai.models.onix.v2

import app.fourdrin.sedai.models.onix.LongTag
import app.fourdrin.sedai.models.onix.OnixDocument
import app.fourdrin.sedai.models.onix.ShortTag
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement


@JacksonXmlRootElement(localName = "ONIXmessage")
class OnixDocumentV2Short : OnixDocument {
}
