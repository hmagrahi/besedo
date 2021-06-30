package models

import enumeratum.{CirceEnum, Enum, EnumEntry}
import enumeratum.EnumEntry.Lowercase

sealed trait Gender extends EnumEntry with Lowercase
object Gender extends Enum[Gender] with CirceEnum[Gender] {
  val values = findValues
  case object Male extends Gender
  case object Female extends Gender
}
