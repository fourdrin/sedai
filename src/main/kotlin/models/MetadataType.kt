package app.fourdrin.sedai.models

sealed class MetadataType

object OnixTwoShort : MetadataType()
object OnixTwoLong : MetadataType()
object OnixThreeShort : MetadataType()
object OnixThreeLong : MetadataType()
object CSVMetadata : MetadataType()
object UnknownMetadata : MetadataType()
