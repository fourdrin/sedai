package app.fourdrin.sedai.models.onix

// Welcome to heck...
// https://onixedit.com/en-us/products/onixedit/onix-tags

sealed class TagV2 {
    object Message {
        const val LONG = "ONIXMessage"
        const val SHORT = "ONIXmessage"
    }

    object Header {
        const val LONG = "Header"
        const val SHORT = "header"

        object FromEANNumber {
            const val LONG = "FromEANNumber"
            const val SHORT = "m172"
        }

        object FromSAN {
            const val LONG = "FromSAN"
            const val SHORT = "m173"
        }

        object SenderIdentifier {
            const val LONG = "SenderIdentifier"
            const val SHORT = "senderidentifier"

            object SenderIDType {
                const val LONG = "SenderIDType"
                const val SHORT = "m379"
            }

            object IDTypeName {
                const val LONG = "IDTypeName"
                const val SHORT = "b233"
            }

            object IDValue {
                const val LONG = "IDValue"
                const val SHORT = "b244"
            }
        }

        object FromCompany {
            const val LONG = "FromCompany"
            const val SHORT = "m174"
        }

        object FromPerson {
            const val LONG = "FromPerson"
            const val SHORT = "m175"
        }

        object FromEmail {
            const val LONG = "FromEmail"
            const val SHORT = "m283"
        }

        object ToEANNumber {
            const val LONG = "ToEANNumber"
            const val SHORT = "m176"
        }

        object ToSAN {
            const val LONG = "ToSAN"
            const val SHORT = "m177"
        }

        object AddresseeIdentifier {
            const val LONG = "AddresseeIdentifier"
            const val SHORT = "addresseeidentifier"

            object AddresseeIDType {
                const val LONG = "AddresseeIDType"
                const val SHORT = "m379"
            }

            object IDTypeName {
                const val LONG = "IDTypeName"
                const val SHORT = "b233"
            }

            object IDValue {
                const val LONG = "IDValue"
                const val SHORT = "b244"
            }
        }

        object ToCompany {
            const val LONG = "ToCompany"
            const val SHORT = "m178"
        }

        object ToPerson {
            const val LONG = "ToPerson"
            const val SHORT = "m179"
        }

        object MessageNumber {
            const val LONG = "MessageNumber"
            const val SHORT = "m180"
        }

        object MessageRepeat {
            const val LONG = "MessageRepeat"
            const val SHORT = "m181"
        }

        object SentDate {
            const val LONG = "SentDate"
            const val SHORT = "m182"
        }

        object MessageNote {
            const val LONG = "MessageNote"
            const val SHORT = "m183"
        }

        object DefaultLanguageOfText {
            const val LONG = "DefaultLanguageOfText"
            const val SHORT = "m184"
        }

        object DefaultPriceTypeCode {
            const val LONG = "DefaultPriceTypeCode"
            const val SHORT = "m185"
        }

        object DefaultCurrencyCode {
            const val LONG = "DefaultCurrencyCode"
            const val SHORT = "m186"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object DefaultLinearUnit {
            const val LONG = "DefaultLinearUnit"
            const val SHORT = "m187"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object DefaultWeightUnit {
            const val LONG = "DefaultWeightUnit"
            const val SHORT = "m188"
        }

        object DefaultClassOfTrade {
            const val LONG = "DefaultClassOfTrade"
            const val SHORT = "m193"
        }
    }

    object Product {
        const val LONG = "Product"
        const val SHORT = "product"

        object RecordReference {
            const val LONG = "RecordReference"
            const val SHORT = "a001"
        }

        object NotificationType {
            const val LONG = "NotificationType"
            const val SHORT = "a002"
        }

        object DeletionCode {
            const val LONG = "DeletionCode"
            const val SHORT = "a198"
        }

        object DeletionText {
            const val LONG = "DeletionText"
            const val SHORT = "a199"
        }

        object RecordSourceType {
            const val LONG = "RecordSourceType"
            const val SHORT = "a194"
        }

        object RecordSourceIdentifierType {
            const val LONG = "RecordSourceIdentifierType"
            const val SHORT = "a195"
        }

        object RecordSourceIdentifier {
            const val LONG = "RecordSourceIdentifier"
            const val SHORT = "a196"
        }

        object RecordSourceName {
            const val LONG = "RecordSourceName"
            const val SHORT = "a197"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object ISBN {
            const val LONG = "ISBN"
            const val SHORT = "b004"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object EAN13 {
            const val LONG = "EAN13"
            const val SHORT = "b005"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object UPC {
            const val LONG = "UPC"
            const val SHORT = "b006"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object PublisherProductNo {
            const val LONG = "PublisherProductNo"
            const val SHORT = "b007"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object ISMN {
            const val LONG = "ISMN"
            const val SHORT = "b008"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object DOI {
            const val LONG = "DOI"
            const val SHORT = "b009"
        }

        object ProductIdentifier {
            const val LONG = "ProductIdentifier"
            const val SHORT = "productidentifier"

            object ProductIDType {
                const val LONG = "ProductIDType"
                const val SHORT = "b221"
            }

            object IDTypeName {
                const val LONG = "IDTypeName"
                const val SHORT = "b233"
            }

            object IDValue {
                const val LONG = "IDValue"
                const val SHORT = "b244"
            }
        }

        object BarCode {
            const val LONG = "Barcode"
            const val SHORT = "b246"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object ReplacesISBN {
            const val LONG = "ReplacesISBN"
            const val SHORT = "b010"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object ReplacesEAN13 {
            const val LONG = "ReplacesEAN13"
            const val SHORT = "b011"
        }

        object ProductForm {
            const val LONG = "ProductForm"
            const val SHORT = "b012"
        }

        object ProductFormDetail {
            const val LONG = "ProductFormDetail"
            const val SHORT = "b333"
        }

        object ProductFormFeature {
            const val LONG  = "ProductFormFeature"
            const val SHORT = "productformfeature"

            object ProductFormFeatureType {
                const val LONG = "ProductFormFeatureType"
                const val SHORT = "b334"
            }

            object ProductFormFeatureValue {
                const val LONG = "ProductFormFeatureValue"
                const val SHORT = "b335"
            }

            object ProductFormFeatureDescription {
                const val LONG = "ProductFormFeatureDescription"
                const val SHORT = "b336"
            }
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object BookFormDetail {
            const val LONG = "BookFormDetail"
            const val SHORT = "b013"
        }

        object ProductPackaging {
            const val LONG = "ProductPackaging"
            const val SHORT = "b225"
        }

        object ProductFormDescription {
            const val LONG = "ProductFormDescription"
            const val SHORT = "b014"
        }

        object NumberOfPieces {
            const val LONG = "NumberOfPieces"
            const val SHORT = "b210"
        }

        object TradeCategory {
            const val LONG = "TradeCategory"
            const val SHORT = "b384"
        }

        object ProductContentType {
            const val LONG = "ProductContentType"
            const val SHORT = "b385"
        }

        object ContainedItem {
            const val LONG = "ContainedItem"
            const val SHORT = "containeditem"

            object ItemQuantity {
                const val LONG = "ItemQuantity"
                const val SHORT = "b015"
            }
        }

        object ProductClassification {
            const val LONG = "ProductClassification"
            const val SHORT = "productclassification"

            object ProductClassificationType {
                const val LONG = "ProductClassificationType"
                const val SHORT = "b274"
            }

            object ProductClassificationCode {
                const val LONG = "ProductClassificationCode"
                const val SHORT = "b275"
            }

            object Percent {
                const val LONG = "Percent"
                const val SHORT = "b337"
            }
        }

        object EpubType {
            const val LONG = "EpubType"
            const val SHORT = "b211"
        }

        object EpubTypeVersion {
            const val LONG = "EpubTypeVersion"
            const val SHORT = "b212"
        }

        object EpubTypeDescription {
            const val LONG = "EpubTypeDescription"
            const val SHORT = "b213"
        }

        object EpubFormat {
            const val LONG = "EpubFormat"
            const val SHORT = "b214"
        }

        object EpubFormatVersion {
            const val LONG = "EpubFormatVersion"
            const val SHORT = "b215"
        }

        object EpubFormatDescription {
            const val LONG = "EpubFormatDescription"
            const val SHORT = "b216"
        }

        object EpubSource {
            const val LONG = "EpubSource"
            const val SHORT = "b278"
        }

        object EpubSourceVersion {
            const val LONG = "EpubSourceVersion"
            const val SHORT = "b279"
        }

        object EpubSourceDescription {
            const val LONG = "EpubSourceDescription"
            const val SHORT = "b280"
        }

        object EpubTypeNote {
            const val LONG = "EpubTypeNote"
            const val SHORT = "b277"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object TextCaseFlag {
            const val LONG = "TextCaseFlag"
            const val SHORT = "b027"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object DistinctiveTitle {
            const val LONG = "DistinctiveTitle"
            const val SHORT = "b028"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object TitlePrefix {
            const val LONG = "TitlePrefix"
            const val SHORT = "b030"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object TitleWithoutPrefix {
            const val LONG = "TitleWithoutPrefix"
            const val SHORT = "b031"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object Subtitle {
            const val LONG = "Subtitle"
            const val SHORT = "b029"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object TranslationOfTitle {
            const val LONG = "TranslationOfTitle"
            const val SHORT = "b032"
        }

        @Deprecated("No longer supported in ONIX 2.1")
        object FormerTitle {
            const val LONG = "FormerTitle"
            const val SHORT = "b033"
        }

        object Title {
            const val LONG = "Title"
            const val SHORT = "title"

            object TitleType {
                const val LONG = "TitleType"
                const val SHORT = "b202"
            }

            object AbbreviatedLength {
                const val LONG = "AbbreviatedLength"
                const val SHORT = "b276"
            }

            @Deprecated("No longer supported in ONIX 2.1")
            object TextCaseFlag {
                const val LONG = "TextCaseFlag"
                const val SHORT = "b027"
            }

            object TitleText {
                const val LONG = "TitleText"
                const val SHORT = "b203"
            }

            object TitlePrefix {
                const val LONG = "TitlePrefix"
                const val SHORT = "b030"
            }

            object TitleWithoutPrefix {
                const val LONG = "TitleWithoutPrefix"
                const val SHORT = "b031"
            }

            object Subtitle {
                const val LONG = "Subtitle"
                const val SHORT = "b029"
            }
        }

        object WorkIdentifier {
            const val LONG = "WorkIdentifier"
            const val SHORT = "workidentifier"

            object WorkIDType {
                const val LONG = "WorkIDType"
                const val SHORT = "b201"
            }

            object IDTypeName {
                const val LONG = "IDTypeName"
                const val SHORT = "b233"
            }

            object IDValue {
                const val LONG = "IDValue"
                const val SHORT = "b244"
            }
        }

        object Website {
            const val LONG = "Website"
            const val SHORT = "website"

            object WebsiteRole {
                const val LONG = "WebsiteRole"
                const val SHORT = "b367"
            }

            object WebsiteDescription {
                const val LONG = "WebsiteDescription"
                const val SHORT = "b294"
            }

            object WebsiteLink {
                const val LONG = "WebsiteLink"
                const val SHORT = "b295"
            }
        }

        object ThesisType {
            const val LONG = "ThesisType"
            const val SHORT = "b368"
        }

        object ThesisPresentedTo {
            const val LONG = "ThesisPresentedTo"
            const val SHORT = "b369"
        }

        object ThesisYear {
            const val LONG = "ThesisYear"
            const val SHORT = "b370"
        }
    }
}

