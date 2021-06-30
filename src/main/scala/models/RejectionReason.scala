package models

import enumeratum.{EnumEntry, _}

sealed abstract class RejectionReason extends EnumEntry

object RejectionReason
    extends Enum[RejectionReason]
    with CirceEnum[RejectionReason] {
  val values = findValues
  case object Underage extends RejectionReason
  case object Scam extends RejectionReason
  case object Contact extends RejectionReason
  case object Nonsense extends RejectionReason
  case object Format extends RejectionReason
}
