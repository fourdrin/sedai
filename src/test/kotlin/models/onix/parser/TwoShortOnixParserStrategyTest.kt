package models.onix.parser

import app.fourdrin.sedai.models.onix.parser.TwoShortOnixParserStrategy
import app.fourdrin.sedai.models.onix.v2.MessageV2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.fail

internal abstract class TwoShortOnixParserStrategyTest {
    val strategy = TwoShortOnixParserStrategy()
    val inputStream = this.javaClass.getResourceAsStream("/onix/2S.xml")
}

internal class ShortSmokeTests : TwoShortOnixParserStrategyTest() {
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

internal class HeaderShortTests : TwoShortOnixParserStrategyTest() {
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

internal class ProductShortTests : TwoShortOnixParserStrategyTest() {
    private val doc = strategy.parseMetadataFile(inputStream)
    private val product = doc.products[0]

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

    @Test
    fun testProductIdentifierComposite() {
        assertEquals(2, product.productIdentifiers.size)

        val productIdentifier1 = product.productIdentifiers[0]
        assertEquals("02", productIdentifier1.productIDType)
        assertEquals("ISBN-10", productIdentifier1.idTypeName)
        assertEquals("0816016356", productIdentifier1.idValue)

        val productIdentifier2 = product.productIdentifiers[1]
        assertEquals("04", productIdentifier2.productIDType)
        assertEquals("UPC", productIdentifier2.idTypeName)
        assertEquals("111", productIdentifier2.idValue)
    }

    @Test
    fun testBarcode() {
        assertEquals(1, product.barcodes?.size)

        val barcode1 = product.barcodes?.get(0)
        assertEquals("abc", barcode1)
    }

    @Test
    fun testReplacesISBN() {
        assertEquals("111", product.replacesISBN)
    }

    @Test
    fun testReplacesEAN13() {
        assertEquals("222", product.replacesEAN13)
    }

    @Test
    fun testProductForm() {
        assertEquals("BB", product.productForm)

        assertEquals(1, product.productFormDetails?.size)
        val productFormDetail1 = product.productFormDetails?.get(0)
        assertEquals("BB Hardback book", productFormDetail1)
    }
}
