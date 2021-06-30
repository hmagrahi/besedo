package rules

import io.circe.{Json, parser}
import models.{Document, ModerationResult, ModerationStatus}

class ParseRule extends Rules[Json, Document, ModerationResult] {

  private val IdNotFound = Document.Id("NaN")
  override def exec(
      json: Json
  ): Either[ModerationResult, Document] = {
    val id = json.hcursor
      .downField("id")
      .as[Document.Id]
      .getOrElse(Document.Id("NaN"))
    json
      .as[Document]
      .fold(
        _ =>
          Left(ModerationResult(id, ModerationStatus.Error, None)): Either[
            ModerationResult,
            Document
          ],
        Right(_)
      )
  }

  override val priority: Int = -99
}
