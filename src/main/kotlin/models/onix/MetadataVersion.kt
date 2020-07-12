package app.fourdrin.sedai.models.onix

import app.fourdrin.sedai.models.MetadataDocument

sealed class MetadataVersion
object TwoShort : MetadataVersion()
object TwoLong : MetadataVersion()
object ThreeShort : MetadataVersion()
object ThreeLong : MetadataVersion()
object Unknown : MetadataVersion()

interface OnixDocument : MetadataDocument {
}

