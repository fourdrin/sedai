package models.onix.parser

import app.fourdrin.sedai.models.onix.parser.TwoLongOnixParserStrategy
import app.fourdrin.sedai.models.onix.parser.TwoShortOnixParserStrategy
import app.fourdrin.sedai.models.onix.v2.MessageV2
import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.fail

internal class TwoShortOnixParserStrategyTest {
    private val strategy = TwoShortOnixParserStrategy()
    private val header = """
    <header>
        <m173>a</m173>
        <m172>1</m172>
        <senderidentifier>
            <m379>01</m379>
            <b233>Foo</b233>
            <b244>Bar</b244>
        </senderidentifier>
        <senderidentifier>
            <m379>01</m379>
            <b233>Biz</b233>
            <b244>Baz</b244>
        </senderidentifier>
        <m174>Foo LLC</m174>
        <m175>foo</m175>
        <m283>foo@foollc.local</m283>
        <m177>b</m177>
        <m176>2</m176>
        <addresseeidentifier>
            <m379>01</m379>
            <b233>Foo</b233>
            <b244>Bar</b244>
        </addresseeidentifier>
        <addresseeidentifier>
            <m379>02</m379>
            <b233>Foo 2</b233>
            <b244>Bar 2</b244>
        </addresseeidentifier>
        <m178>Bar LLC</m178>
        <m179>bar</m179>
        <m180>1234</m180>
        <m181>1</m181>
        <m182>20200618</m182>
        <m183>updates</m183>
        <m187>cm</m187>
        <m188>oz</m188>
        <m184>eng</m184>
        <m185>01</m185>
        <m186>USD</m186>
        <m193>gen</m193>
    </header>
    """.trimIndent()

    private val products = """
    <product>
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
    </product>
        
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
        val doc = strategy.parseMetadataFile(inputStream)

        assertEquals("1234", doc.header.messageNumber)
        assertEquals("1", doc.header.messageRepeat)
        assertEquals("20200618", doc.header.sentDate)
        assertEquals("updates", doc.header.messageNote)
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
    fun testHeaderDeprecatedFields() {
        val doc = strategy.parseMetadataFile(inputStream)

        assertEquals("cm", doc.header.defaultLinearUnit)
        assertEquals("oz", doc.header.defaultWeightUnit)
    }
}