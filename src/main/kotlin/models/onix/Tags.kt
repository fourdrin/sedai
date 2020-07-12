package app.fourdrin.sedai.models.onix

interface LongTag {}

interface ShortTag {
    fun convert(): LongTag
}

private val ShortTagsMappingV2:  Map<String, String> = mapOf(
    "header" to "Header",
    "m172" to "FromEANNumber",
    "m173" to "FromSAN",
    "senderidentifier" to "SenderIdentifier",
    "m379" to "SenderIDType",
    "b233" to "IDTypeName",
    "b244" to "IDValue",
    "m174" to "FromCompany",
    "m175" to "FromPerson",
    "m283" to "FromEmail",
    "m176" to "ToEANNumber",
    "m177" to "ToSAN",
    "addresseeidentifier" to "AddresseeIdentifier",
    "m380" to "AddresseeIDType",
    "b233" to "IDTypeName",
    "b244" to "IDValue",
    "m178" to "ToCompany",
    "m179" to "ToPerson",
    "m180" to "MessageNumber",
    "m181" to "MessageRepeat",
    "m182" to "SentDate",
    "m183" to "MessageNote",
    "m184" to "DefaultLanguageOfText",
    "m185" to "DefaultPriceTypeCode",
    "m186" to "DefaultCurrencyCode",
    "m187" to "DefaultLinearUnit",
    "m188" to "DefaultWeightUnit",
    "m193" to "DefaultClassOfTrade"
)

