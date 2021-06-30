package models

import enumeratum.{CirceEnum, Enum, EnumEntry}
import enumeratum.EnumEntry.Lowercase

sealed abstract class DocumentType(override val entryName: String)
    extends EnumEntry
    with Lowercase

object DocumentType extends Enum[DocumentType] with CirceEnum[DocumentType] {
  val values = findValues
  case object ClassifiedAdsType extends DocumentType("classified")
  case object OnlineDatingProfileType extends DocumentType("profile")
  case object PrivateMessageType extends DocumentType("message")
}
