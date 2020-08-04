package worker.loader

import app.fourdrin.sedai.models.metadata.*
import app.fourdrin.sedai.models.onix.parser.OnixParserStrategy
import app.fourdrin.sedai.models.onix.parser.TwoLongOnixParserStrategy
import app.fourdrin.sedai.worker.loader.ParserRunnable
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Ignore
import org.junit.jupiter.api.Test
import org.mockito.Mockito

internal class ParserRunnableTest {
    private val twoLongInputStream = this.javaClass.getResourceAsStream("/onix/2L.xml")
    private val twoShortInputStream = this.javaClass.getResourceAsStream("/onix/2S.xml")

    private lateinit var metadataParserStrategy: MetadataParserStrategy<MetadataDocument>


    @Test
    fun testRunOnixTwoLong() {
        val parser = Mockito.spy(OnixParserStrategy.build(OnixTwoLong))
        ParserRunnable(parser, twoLongInputStream).run()

        verify(parser).parseMetadataFile(
            twoLongInputStream
        )
    }

    @Test
    fun testRunOnixTwoShort() {
        val parser = Mockito.spy(OnixParserStrategy.build(OnixTwoShort))
        ParserRunnable(parser, twoShortInputStream).run()

        verify(parser).parseMetadataFile(
            twoShortInputStream
        )
    }

    @Test
    @Ignore
    fun testRunOnixThreeLong() {
    }

    @Test
    @Ignore
    fun testRunOnixThreeShort() {
    }
}