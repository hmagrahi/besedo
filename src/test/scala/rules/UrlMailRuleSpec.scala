package rules

import models.Category.{Computers, Entertainment, Food, Miscellaneous, Pets}
import models.Document.{Author, Id, Price, Text}
import models.Gender.Male
import models.ModerationStatus.KO
import models.RejectionReason.{Contact, Scam}
import models.{
  ClassifiedAds,
  ModerationResult,
  OnlineDatingProfile,
  PrivateMessage
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class UrlMailRuleSpec extends AnyFlatSpec with Matchers {
  "an UrlMailRule" should "return ko if the document contains an email in text" in {
    val document = ClassifiedAds(
      Id("id"),
      Author("author"),
      Text("text text besedo@gmail.com text text"),
      Entertainment,
      Price(0f)
    )
    val document2 = OnlineDatingProfile(
      Id("id"),
      Author("author"),
      Text("text text besedo@gmail.com text text"),
      Male,
      Male,
      100,
      Some(Text("besedo@gmail.com text text"))
    )

    val document3 = PrivateMessage(
      Id("id"),
      Author("author"),
      Text("text text besedo@gmail.com text text"),
      "Male",
      Some(Text("besedo@gmail.com text text"))
    )
    val urlMailRule = new UrlEmailRule()
    urlMailRule.exec(document) shouldBe Left(
      ModerationResult(document.id, KO, Some(Contact))
    )
    urlMailRule.exec(document2) shouldBe Left(
      ModerationResult(document.id, KO, Some(Contact))
    )
    urlMailRule.exec(document3) shouldBe Left(
      ModerationResult(document.id, KO, Some(Contact))
    )
  }

  it should "return ko if the document contains an url in text" in {
    val document = ClassifiedAds(
      Id("id"),
      Author("author"),
      Text("text text www.besedo.com text text"),
      Entertainment,
      Price(0f)
    )
    val document2 = OnlineDatingProfile(
      Id("id"),
      Author("author"),
      Text("text text besedo.com text text"),
      Male,
      Male,
      100,
      Some(Text("besedo http://www.besedo.com text text"))
    )

    val document3 = PrivateMessage(
      Id("id"),
      Author("author"),
      Text("text text http://besedo.io text text"),
      "Male",
      Some(Text("besedo besedo.io text text"))
    )
    val urlMailRule = new UrlEmailRule()
    urlMailRule.exec(document) shouldBe Left(
      ModerationResult(document.id, KO, Some(Contact))
    )
    urlMailRule.exec(document2) shouldBe Left(
      ModerationResult(document.id, KO, Some(Contact))
    )
    urlMailRule.exec(document3) shouldBe Left(
      ModerationResult(document.id, KO, Some(Contact))
    )
  }

  it should "return the document if the document does not contains an email in text" in {
    val document = ClassifiedAds(
      Id("id"),
      Author("author"),
      Text("text text text text"),
      Entertainment,
      Price(0f)
    )
    val document2 = OnlineDatingProfile(
      Id("id"),
      Author("author"),
      Text("text text text text"),
      Male,
      Male,
      100,
      Some(Text("besedo text text"))
    )

    val document3 = PrivateMessage(
      Id("id"),
      Author("author"),
      Text("text text text text"),
      "Male",
      Some(Text("text text text"))
    )
    val urlMailRule = new UrlEmailRule()
    urlMailRule.exec(document) shouldBe Right(
      document
    )
    urlMailRule.exec(document2) shouldBe Right(
      document2
    )
    urlMailRule.exec(document3) shouldBe Right(
      document3
    )
  }
}
