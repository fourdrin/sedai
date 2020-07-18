package models.onix.parser

import app.fourdrin.sedai.models.onix.parser.TwoLongOnixParserStrategy
import app.fourdrin.sedai.models.onix.v2.MessageV2
import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.fail

internal class TwoLongOnixParserStrategyTest {
    private val strategy = TwoLongOnixParserStrategy()
    private val header = """
    <Header>
        <FromSAN>a</FromSAN>
        <FromEANNumber>1</FromEANNumber>
        <SenderIdentifier>
            <SenderIDType>01</SenderIDType>
            <IDTypeName>Foo</IDTypeName>
            <IDValue>Bar</IDValue>
        </SenderIdentifier>
        <SenderIdentifier>
            <SenderIDType>01</SenderIDType>
            <IDTypeName>Biz</IDTypeName>
            <IDValue>Baz</IDValue>
        </SenderIdentifier>
        <FromCompany>Foo LLC</FromCompany>
        <FromPerson>foo</FromPerson>
        <FromEmail>foo@foollc.local</FromEmail>
        <ToSAN>b</ToSAN>
        <ToEANNumber>2</ToEANNumber>
        <AddresseeIdentifier>
            <AddresseeIDType>01</AddresseeIDType>
            <IDTypeName>Foo</IDTypeName>
            <IDValue>Bar</IDValue>
        </AddresseeIdentifier>
        <AddresseeIdentifier>
            <AddresseeIDType>02</AddresseeIDType>
            <IDTypeName>Foo 2</IDTypeName>
            <IDValue>Bar 2</IDValue>
        </AddresseeIdentifier>
        <ToCompany>Bar LLC</ToCompany>
        <ToPerson>bar</ToPerson>
        <MessageNumber>1234</MessageNumber>
        <MessageRepeatNumber>1</MessageRepeatNumber>
        <SentDate>20200618</SentDate>
        <DefaultLinearUnit>cm</DefaultLinearUnit>
        <DefaultWeightUnit>oz</DefaultWeightUnit>
        <DefaultLanguageOfText>eng</DefaultLanguageOfText>
        <DefaultPriceTypeCode>01</DefaultPriceTypeCode>
        <DefaultCurrencyCode>USD</DefaultCurrencyCode>
        <DefaultClassOfTrade>gen</DefaultClassOfTrade>
    </Header>
    """.trimIndent()

    private val products = """
    <Product>
        <RecordReference>1234567890</RecordReference>
        <NotificationType>03</NotificationType>
        <ProductIdentifier>
            <ProductIDType>02</ProductIDType>
            <IDValue>0816016356</IDValue>
        </ProductIdentifier>
        <ProductForm>BB</ProductForm>
        <Title>
            <TitleType>01</TitleType>
            <TitleText textcase="02">British English, A to Zed</TitleText>
        </Title>
        <Contributor>
            <SequenceNumber>1</SequenceNumber>
            <ContributorRole>A01</ContributorRole>
            <PersonNameInverted>Schur, Norman W</PersonNameInverted>
            <BiographicalNote>A Harvard graduate in Latin and Italian literature, Norman Schur attended the University of Rome and the Sorbonne before returning to the United States to study law at Harvard and Columbia Law Schools. Now retired from legal practise, Mr Schur is a fluent speaker and writer of both British and American English.</BiographicalNote>
        </Contributor>
        <EditionTypeCode>REV</EditionTypeCode>
        <EditionNumber>3</EditionNumber>
        <Language>
            <LanguageRole>01</LanguageRole>
            <LanguageCode>eng</LanguageCode>
        </Language>
        <NumberOfPages>493</NumberOfPages>
        <BASICMainSubject>REF008000</BASICMainSubject>
        <AudienceCode>01</AudienceCode>
        <OtherText>
            <TextTypeCode>01</TextTypeCode>
            <Text>BRITISH ENGLISH, A TO ZED is the thoroughly updated, revised, and expanded third edition of Norman Schur&rsquo;s highly acclaimed transatlantic dictionary for English speakers. First published as BRITISH SELF-TAUGHT and then as ENGLISH ENGLISH, this collection of Briticisms for Americans, and Americanisms for the British, is a scholarly yet witty lexicon, combining definitions with commentary on the most frequently used and some lesser known words and phrases. Highly readable, it&rsquo;s a snip of a book, and one that sorts out &hellip; through comments in American &hellip; the &ldquo;Queen&rsquo;s English&rdquo; &hellip; confounding as it may seem.</Text>
        </OtherText>
        <OtherText>
            <TextTypeCode>08</TextTypeCode>
            <Text>Norman Schur is without doubt the outstanding authority on the    

      similarities and differences between British and American English. BRITISH ENGLISH, A TO ZED attests not only to his expertise, but also to his undiminished powers to inform, amuse and entertain. &hellip; Laurence Urdang, Editor, VERBATIM, The Language Quarterly, Spring 1988</Text>
        </OtherText>
        <Imprint>
            <ImprintName>Facts on File Publications</ImprintName>
        </Imprint>
        <Publisher>
            <PublishingRole>01</PublishingRole>
            <PublisherName>Facts on File Inc</PublisherName>
        </Publisher>
        <PublicationDate>1987</PublicationDate>
        <Measure>
            <MeasureTypeCode>01</MeasureTypeCode>
            <Measurement>9.25</Measurement>
            <MeasureUnitCode>in</MeasureUnitCode>
        </Measure>
        <Measure>
            <MeasureTypeCode>02</MeasureTypeCode>
            <Measurement>6.25</Measurement>
            <MeasureUnitCode>in</MeasureUnitCode>
        </Measure>
        <Measure>
            <MeasureTypeCode>03</MeasureTypeCode>
            <Measurement>1.2</Measurement>
            <MeasureUnitCode>in</MeasureUnitCode>
        </Measure>
        <SupplyDetail>
            <SupplierSAN>1234567</SupplierSAN>
            <AvailabilityCode>IP</AvailabilityCode>
            <Price>
                <PriceTypeCode>01</PriceTypeCode>
                <PriceAmount>35.00</PriceAmount>
            </Price>
        </SupplyDetail>
    </Product>
        
    """.trimIndent()

    private val onixMessageLong = """
<?xml version="1.0"?>
<!DOCTYPE ONIXMessage SYSTEM "http://www.editeur.org/onix/2.1/reference/onix-international.dtd">
<ONIXMessage>
    $header
    $products
</ONIXMessage> 
    """.trimIndent()


    private val inputStream = IOUtils.toInputStream(onixMessageLong)

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

    @Test
    fun testHeaderFromFields() {
        val doc = strategy.parseMetadataFile(inputStream)
        assertEquals("a", doc.header.fromSAN)
        assertEquals("1", doc.header.fromEANNumber)
        assertEquals("Foo LLC", doc.header.fromCompany)
        assertEquals("foo", doc.header.fromPerson)
        assertEquals("foo@foollc.local", doc.header.fromEmail)
    }

    @Test
    fun testHeaderSenderIdentifiers() {
        val doc = strategy.parseMetadataFile(inputStream)
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
        val doc = strategy.parseMetadataFile(inputStream)
        assertEquals("b", doc.header.toSAN)
        assertEquals("2", doc.header.toEANNumber)
        assertEquals("Bar LLC", doc.header.toCompany)
        assertEquals("bar", doc.header.toPerson)
    }

    @Test
    fun testHeaderAddresseeFields() {
        val doc = strategy.parseMetadataFile(inputStream)
        assertNotNull(doc.header.addresseeIdentifiers)
        assertEquals(2, doc.header.addresseeIdentifiers?.size)
    }

    @Test
    fun testHeaderMessageFields() {
        val doc = strategy.parseMetadataFile(inputStream)
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
    fun testHeaderDefaultFields() {
        val doc = strategy.parseMetadataFile(inputStream)

        assertEquals("eng", doc.header.defaultLanguageOfText)
        assertEquals("01", doc.header.defaultPriceTypeCode)
        assertEquals("USD", doc.header.defaultCurrencyCode)
        assertEquals("gen", doc.header.defaultClassOfTrade)
    }

    @Test
    fun testDeprecatedFields() {
        val doc = strategy.parseMetadataFile(inputStream)

        assertEquals("cm", doc.header.defaultLinearUnit)
        assertEquals("oz", doc.header.defaultWeightUnit)
    }

    @Test
    fun testProductRecordReference() {
        val doc = strategy.parseMetadataFile(inputStream)
        val product = doc.products[0]

        assertEquals("1234567890", product.recordReference)
    }

    @Test
    fun testProductNotificationType() {
        val doc = strategy.parseMetadataFile(inputStream)
        val product = doc.products[0]

        assertEquals("03", product.notificationType)
    }

    @Test
    fun testProductForm() {
        val doc = strategy.parseMetadataFile(inputStream)
        val product = doc.products[0]
        assertEquals("BB", product.productForm)
    }
}