package models

import enumeratum.{CirceEnum, Enum, EnumEntry}
import enumeratum.EnumEntry.Lowercase

sealed trait Category extends EnumEntry with Lowercase
object Category extends Enum[Category] with CirceEnum[Category] {
  val values = findValues
  case object Entertainment extends Category
  case object Pets extends Category
  case object Computers extends Category
  case object Food extends Category
  case object Miscellaneous extends Category
}
