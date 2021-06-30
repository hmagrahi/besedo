package rules

import models.Category.{Computers, Entertainment, Food, Miscellaneous, Pets}
import models.Document.{Author, Id, Price, Text}
import models.Gender.Male
import models.ModerationStatus.KO
import models.RejectionReason.Scam
import models.{
  ClassifiedAds,
  ModerationResult,
  OnlineDatingProfile,
  PrivateMessage
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ConVowRuleSpec extends AnyFlatSpec with Matchers {
  "an ConVow" should "return the document has c / (v + c) between 0.1 and 0.84 for classifieds" in {
    val document = ClassifiedAds(
      Id("id"),
      Author("author"),
      Text("How many con and vow in this text"),
      Entertainment,
      Price(10f)
    )
    val conVowRule = new ConVowRule()
    conVowRule.exec(document) shouldBe Right(document)
  }

  it should "return the document has c / (v + c) between 0.1 and 0.84 for online profiles" in {
    val document = OnlineDatingProfile(
      Id("id"),
      Author("author"),
      Text("a b"),
      Male,
      Male,
      100,
      Some(Text("e t"))
    )

    val conVowRule = new ConVowRule()
    conVowRule.exec(document) shouldBe Right(document)
  }

  it should "return the document has c / (v + c) between 0.2 and 0.76 for private message" in {
    val document = PrivateMessage(
      Id("id"),
      Author("author"),
      Text("o p"),
      "Male",
      Some(Text("ae io ukjh gh"))
    )

    val conVowRule = new ConVowRule()
    conVowRule.exec(document) shouldBe Right(document)
  }

}
