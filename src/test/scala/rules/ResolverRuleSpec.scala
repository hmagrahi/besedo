package rules

import models.Category.Miscellaneous
import models.Document.{Author, Id, Price, Text}
import models.Gender.{Female, Male}
import models.ModerationStatus.{KO, OK}
import models.RejectionReason.Underage
import models.{
  ClassifiedAds,
  ModerationResult,
  OnlineDatingProfile,
  PrivateMessage
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ResolverRuleSpec extends AnyFlatSpec with Matchers {
  "an ResolverRule" should "return ko if the age < 0" in {
    val document = OnlineDatingProfile(
      Id("id"),
      Author("author"),
      Text("text"),
      Male,
      Female,
      -1,
      None
    )
    val resolveRule = new ResolveRule()
    resolveRule.exec(document) shouldBe Left(
      ModerationResult(document.id, KO, None)
    )
  }

  it should "return ok if the document is ok" in {
    val document = OnlineDatingProfile(
      Id("id"),
      Author("author"),
      Text("text"),
      Male,
      Female,
      0,
      None
    )
    val resolveRule = new ResolveRule()
    resolveRule.exec(document) shouldBe Right(document)
  }

  it should "return ko if the price < 0" in {
    val document = ClassifiedAds(
      Id("id"),
      Author("author"),
      Text("text"),
      Miscellaneous,
      Price(-1f)
    )
    val resolveRule = new ResolveRule()
    resolveRule.exec(document) shouldBe Left(
      ModerationResult(document.id, KO, None)
    )
  }

  it should "return ko if the body is empty" in {
    val document = ClassifiedAds(
      Id("id"),
      Author("author"),
      Text("  "),
      Miscellaneous,
      Price(2f)
    )
    val document2 = ClassifiedAds(
      Id("id"),
      Author("author"),
      Text(""),
      Miscellaneous,
      Price(2f)
    )
    val resolveRule = new ResolveRule()
    resolveRule.exec(document) shouldBe Left(
      ModerationResult(document.id, KO, None)
    )
    resolveRule.exec(document2) shouldBe Left(
      ModerationResult(document.id, KO, None)
    )
  }

}
