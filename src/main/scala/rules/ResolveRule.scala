package rules

import models.Document.Author
import models.ModerationStatus.KO
import models.{
  ClassifiedAds,
  Document,
  ModerationResult,
  OnlineDatingProfile,
  PrivateMessage
}

private[rules] class ResolveRule()
    extends Rules[Document, Document, ModerationResult] {

  val index: List[(Document.Id, Author)] = List.empty

  override def exec(
      document: Document
  ): Either[ModerationResult, Document] = {
    for {
      _ <- checkAge(document)
      _ <- checkPrice(document)
      _ <- checkBody(document)
      _ <- checkSubject(document)
    } yield document

  }

  private def checkBody(
      document: Document
  ): Either[ModerationResult, Document] = {
    if (isEmpty(document.body.value)) {
      Left(ModerationResult(document.id, KO, None)): Either[
        ModerationResult,
        Document
      ]
    } else {
      Right(document): Either[ModerationResult, Document]
    }
  }

  private def isEmpty(str: String) = str.trim.isEmpty

  private def checkSubject(
      document: Document
  ): Either[ModerationResult, Document] = {
    document match {
      case PrivateMessage(_, _, _, _, Some(subject))
          if isEmpty(subject.value) =>
        Left(ModerationResult(document.id, KO, None)): Either[
          ModerationResult,
          Document
        ]
      case OnlineDatingProfile(_, _, _, _, _, _, Some(subject))
          if isEmpty(subject.value) =>
        Left(ModerationResult(document.id, KO, None)): Either[
          ModerationResult,
          Document
        ]
      case _ => Right(document): Either[ModerationResult, Document]
    }
  }

  private def checkPrice(
      document: Document
  ): Either[ModerationResult, Document] = {
    document match {
      case ClassifiedAds(_, _, _, _, price) if price.value < 0f =>
        Left(ModerationResult(document.id, KO, None)): Either[
          ModerationResult,
          Document
        ]
      case _ => Right(document): Either[ModerationResult, Document]
    }
  }

  private def checkAge(
      document: Document
  ): Either[ModerationResult, Document] = {
    document match {
      case OnlineDatingProfile(_, _, _, _, _, age, _) if age < 0 =>
        Left(ModerationResult(document.id, KO, None)): Either[
          ModerationResult,
          Document
        ]
      case _ => Right(document): Either[ModerationResult, Document]
    }
  }

  override val priority: Int = 0
}
