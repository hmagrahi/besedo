package rules

import models.Document.Price
import models.{
  Category,
  ClassifiedAds,
  Document,
  ModerationResult,
  ModerationStatus,
  RejectionReason
}

private[rules] class PriceRule
    extends Rules[Document, Document, ModerationResult] {
  override def exec(
      document: Document
  ): Either[ModerationResult, Document] = document match {
    case ClassifiedAds(id, _, _, category, price) =>
      category match {
        case Category.Food if between(price, 10f, 200f)   => Right(document)
        case Category.Pets if between(price, 500f, 1000f) => Right(document)
        case Category.Computers if between(price, 100f, 3500f) =>
          Right(document)
        case Category.Entertainment if between(price, 1f, 1000f) =>
          Right(document)
        case Category.Miscellaneous if between(price, 1f, 100f) =>
          Right(document)
        case _ =>
          Left(
            ModerationResult(
              id,
              ModerationStatus.KO,
              Some(RejectionReason.Scam)
            )
          )
      }
    case _ => Right(document)
  }

  private def between(price: Price, gt: Float, lt: Float): Boolean =
    price.value > gt && price.value < lt

  override val priority: Int = 2
}
