package object models {
  /*

  private def checkId(
      document: Document
  ): Either[ModerationResult, Document] = {
    if (isEmpty(document.id.value)) {
      Left(ModerationResult(document.id, KO, None)): Either[
        ModerationResult,
        Document
      ]
    } else if (isUnique(document.id)) {
      Left(ModerationResult(document.id, KO, None)): Either[
        ModerationResult,
        Document
      ]
    } else {
      Right(document): Either[ModerationResult, Document]
    }
  }

  private def isUnique(id: Document.Id) = index
    .collectFirst { case (id, _) =>
      false
    }
    .getOrElse(true)

  private def checkAuthor(
      document: Document
  ): Either[ModerationResult, Document] = {
    if (isEmpty(document.author.value)) {
      Left(ModerationResult(document.id, KO, None)): Either[
        ModerationResult,
        Document
      ]
    } else if (isUnique(document.author)) {
      Left(ModerationResult(document.id, KO, None)): Either[
        ModerationResult,
        Document
      ]
    } else {
      Right(document): Either[ModerationResult, Document]
    }
  }

  private def isUnique(id: Document.Author) = index
    .collectFirst { case (_, id) =>
      false
    }
    .getOrElse(true)
   */
}
