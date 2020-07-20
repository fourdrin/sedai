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
    }
}

