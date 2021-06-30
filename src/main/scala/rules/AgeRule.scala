package rules

import models.ModerationStatus.KO
import models.{Document, ModerationResult, OnlineDatingProfile, RejectionReason}

private[rules] class AgeRule
    extends Rules[Document, Document, ModerationResult] {
  override def exec(
      document: Document
  ): Either[ModerationResult, Document] =
    document match {
      case OnlineDatingProfile(id, _, _, _, _, age, _) if age < 18 =>
        Left(ModerationResult(id, KO, Some(RejectionReason.Underage)))
      case _ => Right(document): Either[ModerationResult, Document]
    }

  override val priority: Int = 1
}
