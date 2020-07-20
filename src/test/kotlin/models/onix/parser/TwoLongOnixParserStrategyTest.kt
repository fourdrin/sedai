package models.onix.parser

import app.fourdrin.sedai.models.onix.parser.TwoLongOnixParserStrategy
import app.fourdrin.sedai.models.onix.v2.MessageV2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.fail

internal abstract class TwoLongOnixParserStrategyTest {
    val strategy = TwoLongOnixParserStrategy()
    val inputStream = this.javaClass.getResourceAsStream("/onix/2L.xml")
}

internal class LongSmokeTests : TwoLongOnixParserStrategyTest() {
    @Test
    @Tag("smoke")
    fun testSmoke() {
        var doc: MessageV2? = null
        assertDoesNotThrow {
            doc = strategy.parseMetadataFile(inputStream)
        }
        assertNotNull(doc)
        assertNotNull(doc?.header)
        assertNotNull(doc?.products)
        assertEquals(1, doc?.products?.size)
    }
}

internal class HeaderLongTests : TwoLongOnixParserStrategyTest() {
    private val doc = strategy.parseMetadataFile(inputStream)

    @Test
    fun testHeaderFromFields() {
        assertEquals("a", doc.header.fromSAN)
        assertEquals("1", doc.header.fromEANNumber)
        assertEquals("Foo LLC", doc.header.fromCompany)
        assertEquals("foo", doc.header.fromPerson)
        assertEquals("foo@foollc.local", doc.header.fromEmail)
    }

    @Test
    fun testHeaderSenderIdentifiers() {
        assertNotNull(doc.header.senderIdentifiers)
        assertEquals(2, doc.header.senderIdentifiers?.size)

        val senderIdentifier1 = doc.header.senderIdentifiers?.get(0) ?: fail("unable to find sender identifier")

        assertEquals("01", senderIdentifier1.senderIDType)
        assertEquals("Foo", senderIdentifier1.idTypeName)
        assertEquals("Bar", senderIdentifier1.idValue)

        val senderIdentifier2 = doc.header.senderIdentifiers?.get(1) ?: fail("unable to find sender identifier")

        assertEquals("01", senderIdentifier2.senderIDType)
        assertEquals("Biz", senderIdentifier2.idTypeName)
        assertEquals("Baz", senderIdentifier2.idValue)
    }

    @Test
    fun testHeaderToFields() {
        assertEquals("b", doc.header.toSAN)
        assertEquals("2", doc.header.toEANNumber)
        assertEquals("Bar LLC", doc.header.toCompany)
        assertEquals("bar", doc.header.toPerson)
    }

    @Test
    fun testHeaderAddresseeFields() {
        assertNotNull(doc.header.addresseeIdentifiers)
        assertEquals(2, doc.header.addresseeIdentifiers?.size)

        val addresseeIdentifier1 =
            doc.header.addresseeIdentifiers?.get(0) ?: fail("unable to find addressee identifier")

        assertEquals("01", addresseeIdentifier1.addresseeIDType)
        assertEquals("Foo", addresseeIdentifier1.idTypeName)
        assertEquals("Bar", addresseeIdentifier1.idValue)

        val addresseeIdentifier2 =
            doc.header.addresseeIdentifiers?.get(1) ?: fail("unable to find addressee identifier")

        assertEquals("02", addresseeIdentifier2.addresseeIDType)
        assertEquals("Foo 2", addresseeIdentifier2.idTypeName)
        assertEquals("Bar 2", addresseeIdentifier2.idValue)
    }

    @Test
    fun testHeaderMessageFields() {
        assertEquals("1234", doc.header.messageNumber)
        assertEquals("1", doc.header.messageRepeat)
        assertEquals("20200618", doc.header.sentDate)
        assertEquals("updates", doc.header.messageNote)
    }

    @Test
    fun testHeaderDefaultFields() {
        assertEquals("eng", doc.header.defaultLanguageOfText)
        assertEquals("01", doc.header.defaultPriceTypeCode)
        assertEquals("USD", doc.header.defaultCurrencyCode)
        assertEquals("gen", doc.header.defaultClassOfTrade)
    }

    @Test
    fun testHeaderDeprecatedFields() {
        assertEquals("cm", doc.header.defaultLinearUnit)
        assertEquals("oz", doc.header.defaultWeightUnit)
    }
}

internal class ProductLongTests : TwoLongOnixParserStrategyTest() {
    private val doc = strategy.parseMetadataFile(inputStream)
    private val product = doc.products[0]

    @Test
    fun testProductRecordReference() {
        assertEquals("1234567890", product.recordReference)
    }

    @Test
    fun testProductNotificationType() {
        assertEquals("03", product.notificationType)
    }

    @Test
    fun testProductDeletionInformation() {
        assertEquals("00", product.deletionCode)
        assertEquals("Removed from catalog", product.deletionText)
    }

    @Test
    fun testProductRecordSourceDetails() {
        assertEquals("01", product.recordSourceType)
        assertEquals("03", product.recordSourceIdentifierType)
        assertEquals("ABC", product.recordSourceIdentifier)
        assertEquals("Rando Publisher", product.recordSourceName)
    }

    @Test
    fun testProductDeprecatedNumbers() {
        assertEquals("1", product.isbn)
        assertEquals("2", product.ean13)
        assertEquals("3", product.upc)
        assertEquals("4", product.publisherProductNo)
        assertEquals("5", product.ismn)
        assertEquals("6", product.doi)
    }
}
