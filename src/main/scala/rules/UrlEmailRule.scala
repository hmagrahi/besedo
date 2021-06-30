package rules

import models.Document.Text
import models.ModerationStatus.KO
import models.RejectionReason.Contact
import models.{Document, ModerationResult, PrivateMessage}

private[rules] class UrlEmailRule
    extends Rules[Document, Document, ModerationResult] {

  private val EmailRegex =
    """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"""
  private val UrlRegex =
    """(?:(?:https?|ftp):\/\/)?[\w/\-?=%.]+\.[\w/\-&?=%.]+"""
  override def exec(document: Document): Either[ModerationResult, Document] =
    document match {
      case PrivateMessage(id, _, body, _, Some(subject)) =>
        hasEmailOrUrl(id, subject)
          .flatMap(_ => hasEmailOrUrl(id, body))
          .map(_ => document)
      case document =>
        hasEmailOrUrl(document.id, document.body).map(_ => document)
    }

  private def hasEmailOrUrl(
      id: Document.Id,
      text: Text
  ): Either[ModerationResult, Unit] = {
    text.value
      .split(" ")
      .find(str => str.matches(EmailRegex) | str.matches(UrlRegex))
      .fold(Right(()): Either[ModerationResult, Unit])(_ =>
        Left(ModerationResult(id, KO, Some(Contact)))
      )

  }

  override val priority: Int = 3
}
