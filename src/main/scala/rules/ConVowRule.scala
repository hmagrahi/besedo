package rules

import models.Document.Text
import models.ModerationStatus.KO
import models.RejectionReason.Contact
import models.{
  ClassifiedAds,
  Document,
  ModerationResult,
  OnlineDatingProfile,
  PrivateMessage,
  RejectionReason
}
/*
consonants and  vowels
formula: `min < c / (v + c) < max`, where:
 */
private[rules] class ConVowRule
    extends Rules[Document, Document, ModerationResult] {

  private lazy val Vowels =
    Array('a', 'e', 'i', 'o', 'u', 'y', 'A', 'E', 'I', 'O', 'U', 'Y')
  type VowelConsonant = (Int, Int)

  override def exec(document: Document): Either[ModerationResult, Document] =
    document match {
      case ClassifiedAds(_, _, body, _, _)
          if between(formula(counter(body)), 0.1f, 0.84f) =>
        Right(document)
      case PrivateMessage(_, _, body, _, Some(subject))
          if between(formula(counter(body)), 0.2f, 0.76f) && between(
            formula(counter(subject)),
            0.2f,
            0.76f
          ) =>
        Right(document)
      case PrivateMessage(_, _, body, _, None)
          if between(formula(counter(body)), 0.2f, 0.76f) =>
        Right(document)
      case OnlineDatingProfile(_, _, body, _, _, _, Some(subject))
          if between(formula(counter(body)), 0.1f, 0.84f) && between(
            formula(counter(subject)),
            0.1f,
            0.84f
          ) =>
        Right(document)
      case _ =>
        Left(ModerationResult(document.id, KO, Some(RejectionReason.Nonsense)))
    }

  private def counter(
      text: Text
  ): VowelConsonant = {
    var vs = 0
    var vc = 0
    text.value.iterator.foreach(c =>
      if (Vowels.contains(c)) { vs = vs + 1 }
      else { vc = vc + 1 }
    )
    (vs, vc)
  }

  private def formula(n: VowelConsonant): Float = {
    n._1.toFloat / (n._1 + n._2).toFloat
  }
  private def between(n: Float, min: Float, max: Float) = n > min && n < max

  override val priority: Int = 4
}
