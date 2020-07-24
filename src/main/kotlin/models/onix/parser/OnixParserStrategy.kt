package app.fourdrin.sedai.models.onix.parser

import app.fourdrin.sedai.models.metadata.*
import com.ctc.wstx.stax.WstxInputFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import javax.xml.stream.XMLInputFactory

abstract class OnixParserStrategy<out T: MetadataDocument> :
    MetadataParserStrategy<T> {
    private val inputFactory = WstxInputFactory()

    init {
        // We can skip this since:
        // a) ONIX 2.1 DTD returns a 404
        // b) EDITUR specifically mentions that an ONIX' DTD is for internal use only.
        inputFactory.setProperty(
            XMLInputFactory.SUPPORT_DTD,
            false
        )

        // Disable replacing entities since there is no guarantee ONIX files will declare them. Bad spec is bad.
        inputFactory.setProperty(
            XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,
            false
        )
    }

    private val xmlFactory = XmlFactory(inputFactory)

    private val module  = JacksonXmlModule()
    init {
        module.setDefaultUseWrapper(false)
    }

    protected val objectMapper: ObjectMapper  = XmlMapper(xmlFactory, module).registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
        .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)

    companion object Factory {
        fun build(metadataType: MetadataType): OnixParserStrategy<MetadataDocument> {
            return when (metadataType) {
                is OnixTwoLong -> TwoLongOnixParserStrategy()
                is OnixTwoShort -> TwoShortOnixParserStrategy()
                else -> TODO()
            }
        }
    }
}