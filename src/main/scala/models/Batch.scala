package models

import io.circe.{Codec, Json}
import io.circe.generic.semiauto.deriveCodec
import io.circe.Encoder._

case class Batch(id: String, content: List[Json])
case class ModerateBatch(id: String, content: List[ModerationResult])
object Batch {
  implicit val batchCodec: Codec.AsObject[Batch] =
    deriveCodec[Batch]
}

object ModerateBatch {
  implicit val ModerateBatchCodec: Codec.AsObject[ModerateBatch] =
    deriveCodec[ModerateBatch]
}

case class ModerationResult(
    id: Document.Id,
    status: ModerationStatus,
    reason: Option[RejectionReason]
)

object ModerationResult {
  implicit val ModerationResultCodec: Codec.AsObject[ModerationResult] =
    deriveCodec[ModerationResult]
}
