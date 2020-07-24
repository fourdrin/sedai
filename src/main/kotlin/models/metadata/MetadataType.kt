package app.fourdrin.sedai.models.metadata

sealed class MetadataType

object OnixTwoShort : MetadataType()
object OnixTwoLong : MetadataType()
object OnixThreeShort : MetadataType()
object OnixThreeLong : MetadataType()
object CSVMetadata : MetadataType()
object UnknownMetadata : MetadataType()
