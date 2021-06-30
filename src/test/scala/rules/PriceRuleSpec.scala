package rules

import models.Category.{Computers, Entertainment, Food, Miscellaneous, Pets}
import models.Document.{Author, Id, Price, Text}
import models.ModerationStatus.KO
import models.RejectionReason.Scam
import models.{ClassifiedAds, ModerationResult}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PriceRuleSpec extends AnyFlatSpec with Matchers {
  "an PriceRule" should "return the document after validate that a document the price between 1F and 1000F for entertainment" in {
    for {
      price <- Range(2, 999)
    } yield {
      val document = ClassifiedAds(
        Id("id"),
        Author("author"),
        Text("text"),
        Entertainment,
        Price(price)
      )
      val priceRule = new PriceRule()
      priceRule.exec(document) shouldBe Right(document)
    }
  }

  it should "return the document after validate that a document the price between 500F and 1000F for pets" in {
    for {
      price <- Range(501, 999)
    } yield {
      val document = ClassifiedAds(
        Id("id"),
        Author("author"),
        Text("text"),
        Pets,
        Price(price)
      )
      val priceRule = new PriceRule()
      priceRule.exec(document) shouldBe Right(document)
    }
  }

  it should "return the document after validate that a document the price between 100F and 3500F for computers" in {
    for {
      price <- Range(101, 3499)
    } yield {
      val document = ClassifiedAds(
        Id("id"),
        Author("author"),
        Text("text"),
        Computers,
        Price(price)
      )
      val priceRule = new PriceRule()
      priceRule.exec(document) shouldBe Right(document)
    }
  }

  it should "return the document after validate that a document the price between 10F and 200F for food" in {
    for {
      price <- Range(11, 199)
    } yield {
      val document = ClassifiedAds(
        Id("id"),
        Author("author"),
        Text("text"),
        Food,
        Price(price)
      )
      val priceRule = new PriceRule()
      priceRule.exec(document) shouldBe Right(document)
    }
  }

  it should "return the document after validate that a document the price between 1F and 100F for miscellaneous" in {
    for {
      price <- Range(2, 100)
    } yield {
      val document = ClassifiedAds(
        Id("id"),
        Author("author"),
        Text("text"),
        Miscellaneous,
        Price(price)
      )
      val priceRule = new PriceRule()
      priceRule.exec(document) shouldBe Right(document)
    }
  }

  it should "return the ko if the price is not between 1F and 100F for miscellaneous" in {
    for {
      price <- Range(100, 200)
    } yield {
      val document = ClassifiedAds(
        Id("id"),
        Author("author"),
        Text("text"),
        Miscellaneous,
        Price(price)
      )
      val priceRule = new PriceRule()
      priceRule.exec(document) shouldBe Left(
        ModerationResult(document.id, KO, Some(Scam))
      )
    }
  }
}
