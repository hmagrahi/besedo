package models

import enumeratum.EnumEntry.Lowercase
import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed trait ModerationStatus extends EnumEntry with Lowercase
object ModerationStatus
    extends Enum[ModerationStatus]
    with CirceEnum[ModerationStatus] {
  val values = findValues
  case object OK extends ModerationStatus
  case object KO extends ModerationStatus
  case object Error extends ModerationStatus
}
