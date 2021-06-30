package models

import io.circe.{Codec, Decoder, Encoder}
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec
import io.circe.generic.semiauto.deriveCodec
import models.Document.{Author, Id, Price, Text}
import cats.syntax.functor._
import io.circe.{Decoder, Encoder}, io.circe.generic.auto._
import io.circe.syntax._

sealed trait Document {
  val id: Document.Id
  val `type`: DocumentType
  val author: Author
  val body: Text
}

case class ClassifiedAds(
    override val id: Id,
    override val author: Author,
    override val body: Text,
    category: Category,
    price: Price
) extends Document {
  override val `type` = DocumentType.ClassifiedAdsType
}

object ClassifiedAds {
  implicit val classifiedAdsCodec: Codec.AsObject[ClassifiedAds] =
    deriveCodec[ClassifiedAds]
}

case class OnlineDatingProfile(
    override val id: Id,
    override val author: Author,
    override val body: Text,
    gender: Gender,
    seeks: Gender,
    age: Int,
    subject: Option[Text]
) extends Document {
  override val `type` = DocumentType.OnlineDatingProfileType
}

object OnlineDatingProfile {
  implicit val onlineDatingCodec: Codec.AsObject[OnlineDatingProfile] =
    deriveCodec[OnlineDatingProfile]
}

case class PrivateMessage(
    override val id: Id,
    override val author: Author,
    override val body: Text,
    to: String,
    subject: Option[Text]
) extends Document {
  override val `type` = DocumentType.PrivateMessageType
}

object PrivateMessage {
  implicit val privateCodec: Codec.AsObject[PrivateMessage] =
    deriveCodec[PrivateMessage]
}

object Document {
  case class Id(value: String)
  case class Author(value: String)
  case class Price(value: Float)
  case class Text(value: String)
  implicit val encodeDocument: Encoder[Document] = Encoder.instance {
    case opd @ OnlineDatingProfile(_, _, _, _, _, _, _) => opd.asJson
    case pm @ PrivateMessage(_, _, _, _, _)             => pm.asJson
    case ca @ ClassifiedAds(_, _, _, _, _)              => ca.asJson
  }

  implicit val decodeDocument: Decoder[Document] =
    List[Decoder[Document]](
      Decoder[OnlineDatingProfile].widen,
      Decoder[PrivateMessage].widen,
      Decoder[ClassifiedAds].widen
    ).reduceLeft(_ or _)

  implicit val DocumentIdCodec: Codec[Document.Id] =
    deriveUnwrappedCodec[Document.Id]
  implicit val AuthorCodec: Codec[Document.Author] =
    deriveUnwrappedCodec[Document.Author]
  implicit val PriceCodec: Codec[Document.Price] =
    deriveUnwrappedCodec[Document.Price]
  implicit val TextCodec: Codec[Document.Text] =
    deriveUnwrappedCodec[Document.Text]
}
