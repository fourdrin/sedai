package models.onix.parser

import app.fourdrin.sedai.models.onix.parser.TwoShortOnixParserStrategy
import app.fourdrin.sedai.models.onix.v2.MessageV2
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

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

        assertEquals("04", product.bookFormDetail)
        assertEquals("05", product.productPackaging)
        assertEquals("3 volumes with 2 audiocassettes", product.productFormDescription)
        assertEquals(3, product.numberOfPieces)
        assertEquals("03", product.tradeCategory)

        assertEquals(1, product.productContentTypes?.size)
        assertEquals("01", product.productContentTypes?.get(0))
    }

    @Test
    fun testProductFormFeature() {
        assertEquals(2, product.productFormFeatures?.size)

        val productFormFeatures1 = product.productFormFeatures?.get(0)

        assertEquals("02 Page edge color", productFormFeatures1?.productFormFeatureType)
        assertEquals("BLK Black (binding color)", productFormFeatures1?.productFormFeatureValue)
        Assertions.assertNull(productFormFeatures1?.productFormFeatureDescription)

        val productFormFeatures2 = product.productFormFeatures?.get(1)

        assertEquals("02 Page edge color", productFormFeatures2?.productFormFeatureType)
        Assertions.assertNull(productFormFeatures2?.productFormFeatureValue)
        assertEquals("11pt Helvetica", productFormFeatures2?.productFormFeatureDescription)
    }

    @Test
    fun testContainedItem() {
        assertEquals(1, product.containedItems?.size)

        val containedItem = product.containedItems?.get(0)

        assertEquals("1", containedItem?.isbn)
        assertEquals("2", containedItem?.ean13)
        assertEquals(1, containedItem?.productIdentifiers?.size)

        val productIdentifier1 = containedItem?.productIdentifiers?.get(0)
        assertEquals("02", productIdentifier1?.productIDType)
        assertEquals("ISBN-10", productIdentifier1?.idTypeName)
        assertEquals("0816016356", productIdentifier1?.idValue)

        assertEquals("BB", containedItem?.productForm)
        assertEquals(1, containedItem?.productFormDetails?.size)
        assertEquals("04", containedItem?.bookFormDetail)
        assertEquals("05", containedItem?.productPackaging)
        assertEquals("3 volumes with 2 audiocassettes", containedItem?.productFormDescription)
        assertEquals(3, containedItem?.numberOfPieces)
        assertEquals("03", containedItem?.tradeCategory)
        assertEquals(1, containedItem?.productContentTypes?.size)
        assertEquals("01", containedItem?.productContentTypes?.get(0))

        val productFormDetail1 = containedItem?.productFormDetails?.get(0)
        assertEquals("BB Hardback book", productFormDetail1)

        assertEquals(1, containedItem?.itemsQuantity)
    }

    @Test
    fun testProductClassifications() {
        assertEquals(1, product.productClassifications?.size)

        val productClassification = product.productClassifications?.get(0)

        assertEquals("02", productClassification?.productClassificationType)
        assertEquals("55101514", productClassification?.productClassificationCode)
        assertEquals("66.67", productClassification?.percent)
    }

    @Test
    fun testEpublicationDetails() {
        assertEquals("004", product.epubType)
        assertEquals("2.1", product.epubTypeVersion)
        assertEquals("Adobe Ebook Reader", product.epubTypeDescription)
        assertEquals("02", product.epubFormat)
        assertEquals("2.1", product.epubFormatVersion)
        assertEquals("Screen optimized PDF, with low-res figures", product.epubFormatDescription)
        assertEquals("02", product.epubSource)
        assertEquals("2.1", product.epubSourceVersion)
        assertEquals("Screen optimized PDF, with low-res figures", product.epubSourceDescription)
        assertEquals("First appearance of this title in Microsoft Reader format", product.epubTypeNote)
    }
}
