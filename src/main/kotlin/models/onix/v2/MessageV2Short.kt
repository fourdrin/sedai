package app.fourdrin.sedai.models.onix.v2

import app.fourdrin.sedai.models.onix.TagV2
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = TagV2.Message.SHORT)
data class MessageV2Short(
    @JacksonXmlProperty(localName = TagV2.Header.SHORT)
    override val header: HeaderShort,

    @JacksonXmlProperty(localName = TagV2.Product.SHORT)
    override val products: List<ProductShort>
) : MessageV2

// Header

data class HeaderShort(
    @JacksonXmlProperty(localName = TagV2.Header.FromSAN.SHORT)
    override val fromSAN: String? = "",

    @JacksonXmlProperty(localName = TagV2.Header.FromEANNumber.SHORT)
    override val fromEANNumber: String? = "",

    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.SHORT)
    override val senderIdentifiers: List<SenderIdentifierShort>? = listOf(),

    @JacksonXmlProperty(localName = TagV2.Header.FromCompany.SHORT)
    override val fromCompany: String?,

    @JacksonXmlProperty(localName = TagV2.Header.FromPerson.SHORT)
    override val fromPerson: String?,

    @JacksonXmlProperty(localName = TagV2.Header.FromEmail.SHORT)
    override val fromEmail: String?,

    @JacksonXmlProperty(localName = TagV2.Header.ToEANNumber.SHORT)
    override val toEANNumber: String?,

    @JacksonXmlProperty(localName = TagV2.Header.ToSAN.SHORT)
    override val toSAN: String?,

    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.SHORT)
    override val addresseeIdentifiers: List<AddresseeIdentifierShort>?,

    @JacksonXmlProperty(localName = TagV2.Header.ToCompany.SHORT)
    override val toCompany: String?,

    @JacksonXmlProperty(localName = TagV2.Header.ToPerson.SHORT)
    override val toPerson: String?,

    @JacksonXmlProperty(localName = TagV2.Header.MessageNumber.SHORT)
    override val messageNumber: String?,

    @JacksonXmlProperty(localName = TagV2.Header.MessageRepeat.SHORT)
    override val messageRepeat: String?,

    @JacksonXmlProperty(localName = TagV2.Header.SentDate.SHORT)
    override val sentDate: String,

    @JacksonXmlProperty(localName = TagV2.Header.MessageNote.SHORT)
    override val messageNote: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultLanguageOfText.SHORT)
    override val defaultLanguageOfText: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultPriceTypeCode.SHORT)
    override val defaultPriceTypeCode: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultCurrencyCode.SHORT)
    override val defaultCurrencyCode: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultLinearUnit.SHORT)
    override val defaultLinearUnit: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultWeightUnit.SHORT)
    override val defaultWeightUnit: String?,

    @JacksonXmlProperty(localName = TagV2.Header.DefaultClassOfTrade.SHORT)
    override val defaultClassOfTrade: String?
) : Header

data class SenderIdentifierShort(
    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.SenderIDType.SHORT)
    override val senderIDType: String,

    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.IDTypeName.SHORT)
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = TagV2.Header.SenderIdentifier.IDValue.SHORT)
    override val idValue: String?
) : SenderIdentifier

@JsonIgnoreProperties(ignoreUnknown = true)
data class AddresseeIdentifierShort(
    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.AddresseeIDType.SHORT)
    override val addresseeIDType: String,

    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.IDTypeName.SHORT)
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = TagV2.Header.AddresseeIdentifier.IDValue.SHORT)
    override val idValue: String?

) : AddresseeIdentifier

// Products

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProductShort(
    @JacksonXmlProperty(localName = TagV2.Product.RecordReference.SHORT)
    override val recordReference: String,

    @JacksonXmlProperty(localName = TagV2.Product.NotificationType.SHORT)
    override val notificationType: String,

    @JacksonXmlProperty(localName = TagV2.Product.DeletionCode.SHORT)
    override val deletionCode: String?,

    @JacksonXmlProperty(localName = TagV2.Product.DeletionText.SHORT)
    override val deletionText: String?,

    @JacksonXmlProperty(localName = TagV2.Product.RecordSourceType.SHORT)
    override val recordSourceType: String?,

    @JacksonXmlProperty(localName = TagV2.Product.RecordSourceIdentifierType.SHORT)
    override val recordSourceIdentifierType: String?,

    @JacksonXmlProperty(localName = TagV2.Product.RecordSourceIdentifier.SHORT)
    override val recordSourceIdentifier: String?,

    @JacksonXmlProperty(localName = TagV2.Product.RecordSourceName.SHORT)
    override val recordSourceName: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ISBN.SHORT)
    override val isbn: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EAN13.SHORT)
    override val ean13: String?,

    @JacksonXmlProperty(localName = TagV2.Product.UPC.SHORT)
    override val upc: String?,

    @JacksonXmlProperty(localName = TagV2.Product.PublisherProductNo.SHORT)
    override val publisherProductNo: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ISMN.SHORT)
    override val ismn: String?,

    @JacksonXmlProperty(localName = TagV2.Product.DOI.SHORT)
    override val doi: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductIdentifier.SHORT)
    override val productIdentifiers: List<ProductIdentifierShort>,

    @JacksonXmlProperty(localName = TagV2.Product.BarCode.SHORT)
    override val barcodes: List<String>?,

    @JacksonXmlProperty(localName = TagV2.Product.ReplacesISBN.SHORT)
    override var replacesISBN: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ReplacesEAN13.SHORT)
    override var replacesEAN13: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductForm.SHORT)
    override val productForm: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormDetail.SHORT)
    override val productFormDetails: List<String>?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormFeature.SHORT)
    override val productFormFeatures: List<ProductFormFeatureShort>?,

    @JacksonXmlProperty(localName = TagV2.Product.BookFormDetail.SHORT)
    override val bookFormDetail: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductPackaging.SHORT)
    override val productPackaging: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormDescription.SHORT)
    override val productFormDescription: String?,

    @JacksonXmlProperty(localName = TagV2.Product.NumberOfPieces.SHORT)
    override val numberOfPieces: Int?,

    @JacksonXmlProperty(localName = TagV2.Product.TradeCategory.SHORT)
    override val tradeCategory: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductContentType.SHORT)
    override val productContentTypes: List<String>?,

    @JacksonXmlProperty(localName = TagV2.Product.ContainedItem.SHORT)
    override val containedItems: List<ContainedItemShort>?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductClassification.SHORT)
    override val productClassifications: List<ProductClassificationShort>?,

    @JacksonXmlProperty(localName = TagV2.Product.EpubType.SHORT)
    override val epubType: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EpubTypeVersion.SHORT)
    override val epubTypeVersion: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EpubTypeDescription.SHORT)
    override val epubTypeDescription: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EpubFormat.SHORT)
    override val epubFormat: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EpubFormatVersion.SHORT)
    override val epubFormatVersion: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EpubFormatDescription.SHORT)
    override val epubFormatDescription: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EpubSource.SHORT)
    override val epubSource: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EpubSourceVersion.SHORT)
    override val epubSourceVersion: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EpubSourceDescription.SHORT)
    override val epubSourceDescription: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EpubTypeNote.SHORT)
    override val epubTypeNote: String?,

    @JacksonXmlProperty(localName = TagV2.Product.TextCaseFlag.SHORT)
    override val textCaseFlag: String?,

    @JacksonXmlProperty(localName = TagV2.Product.DistinctiveTitle.SHORT)
    override val distinctiveTitle: String?,

    @JacksonXmlProperty(localName = TagV2.Product.TitlePrefix.SHORT)
    override val titlePrefix: String?,

    @JacksonXmlProperty(localName = TagV2.Product.TitleWithoutPrefix.SHORT)
    override val titleWithoutPrefix: String?,

    @JacksonXmlProperty(localName = TagV2.Product.Subtitle.SHORT)
    override val subtitle: String?,

    @JacksonXmlProperty(localName = TagV2.Product.TranslationOfTitle.SHORT)
    override val translationOfTitle: String?,

    @JacksonXmlProperty(localName = TagV2.Product.FormerTitle.SHORT)
    override val formerTitle: String?,

    @JacksonXmlProperty(localName = TagV2.Product.Title.SHORT)
    override val titles: List<TitleCompositeShort>?,

    @JacksonXmlProperty(localName = TagV2.Product.WorkIdentifier.SHORT)
    override val workIdentifiers: List<WorkIdentifierShort>?,

    @JacksonXmlProperty(localName = TagV2.Product.Website.SHORT)
    override val websites: List<WebsiteShort>?,

    @JacksonXmlProperty(localName = TagV2.Product.ThesisType.SHORT)
    override val thesisType: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ThesisPresentedTo.SHORT)
    override val thesisPresentedTo: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ThesisYear.SHORT)
    override val thesisYear: String?
) : Product

data class ProductIdentifierShort(
    @JacksonXmlProperty(localName = TagV2.Product.ProductIdentifier.ProductIDType.SHORT)
    override val productIDType: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductIdentifier.IDTypeName.SHORT)
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductIdentifier.IDValue.SHORT)
    override val idValue: String?
) : ProductIdentifier

data class ProductFormFeatureShort(
    @JacksonXmlProperty(localName = TagV2.Product.ProductFormFeature.ProductFormFeatureType.SHORT)
    override val productFormFeatureType: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormFeature.ProductFormFeatureValue.SHORT)
    override val productFormFeatureValue: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormFeature.ProductFormFeatureDescription.SHORT)
    override val productFormFeatureDescription: String?
) : ProductFormFeature

data class ContainedItemShort(
    @JacksonXmlProperty(localName = TagV2.Product.ISBN.SHORT)
    override val isbn: String?,

    @JacksonXmlProperty(localName = TagV2.Product.EAN13.SHORT)
    override val ean13: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductIdentifier.SHORT)
    override val productIdentifiers: List<ProductIdentifierShort>,

    @JacksonXmlProperty(localName = TagV2.Product.ProductForm.SHORT)
    override val productForm: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormDetail.SHORT)
    override val productFormDetails: List<String>?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormFeature.SHORT)
    override val productFormFeatures: List<ProductFormFeatureShort>?,

    @JacksonXmlProperty(localName = TagV2.Product.BookFormDetail.SHORT)
    override val bookFormDetail: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductPackaging.SHORT)
    override val productPackaging: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductFormDescription.SHORT)
    override val productFormDescription: String?,

    @JacksonXmlProperty(localName = TagV2.Product.NumberOfPieces.SHORT)
    override val numberOfPieces: Int?,

    @JacksonXmlProperty(localName = TagV2.Product.TradeCategory.SHORT)
    override val tradeCategory: String?,

    @JacksonXmlProperty(localName = TagV2.Product.ProductContentType.SHORT)
    override val productContentTypes: List<String>?,

    @JacksonXmlProperty(localName = TagV2.Product.ContainedItem.ItemQuantity.SHORT)
    override val itemsQuantity: Int?
) : ContainedItem

data class ProductClassificationShort(
    @JacksonXmlProperty(localName = TagV2.Product.ProductClassification.ProductClassificationType.SHORT)
    override val productClassificationType: String,

    @JacksonXmlProperty(localName = TagV2.Product.ProductClassification.ProductClassificationCode.SHORT)
    override val productClassificationCode: String,

    @JacksonXmlProperty(localName = TagV2.Product.ProductClassification.Percent.SHORT)
    override val percent: String?
) : ProductClassification

data class TitleCompositeShort(
    @JacksonXmlProperty(localName = TagV2.Product.Title.TitleType.SHORT)
    override val titleType: String,

    @JacksonXmlProperty(localName = TagV2.Product.Title.AbbreviatedLength.SHORT)
    override val abbreviatedLength: String?,

    @JacksonXmlProperty(localName = TagV2.Product.Title.TextCaseFlag.SHORT)
    override val textCaseFlag: String?,

    @JacksonXmlProperty(localName = TagV2.Product.Title.TitleText.SHORT)
    override val titleText: ProductTitle?,

    @JacksonXmlProperty(localName = TagV2.Product.Title.TitlePrefix.SHORT)
    override val titlePrefix: ProductTitle?,

    @JacksonXmlProperty(localName = TagV2.Product.Title.TitleWithoutPrefix.SHORT)
    override val titleWithoutPrefix: ProductTitle?,

    @JacksonXmlProperty(localName = TagV2.Product.Title.Subtitle.SHORT)
    override val subtitle: ProductTitle?
) : TitleComposite

data class WorkIdentifierShort(
    @JacksonXmlProperty(localName = TagV2.Product.WorkIdentifier.WorkIDType.SHORT)
    override val workIDType: String?,

    @JacksonXmlProperty(localName = TagV2.Product.WorkIdentifier.IDTypeName.SHORT)
    override val idTypeName: String?,

    @JacksonXmlProperty(localName = TagV2.Product.WorkIdentifier.IDValue.SHORT)
    override val idValue: String?
) : WorkIdentifier

data class WebsiteShort(
    @JacksonXmlProperty(localName = TagV2.Product.Website.WebsiteRole.SHORT)
    override val websiteRole: String?,

    @JacksonXmlProperty(localName = TagV2.Product.Website.WebsiteDescription.SHORT)
    override val websiteDescription: String?,

    @JacksonXmlProperty(localName = TagV2.Product.Website.WebsiteLink.SHORT)
    override val websiteLink: String?
) : Website
