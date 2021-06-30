package rules

import models.Document.Id
import models.Document.Author
import models.Document.Text
import models.Gender.Male
import models.Gender.Female
import models.ModerationStatus.KO
import models.RejectionReason.Underage
import models.{ModerationResult, OnlineDatingProfile, PrivateMessage}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AgeRuleSpec extends AnyFlatSpec with Matchers {
  "an AgeRule" should "return the document after validate that a document have an age > 18" in {
    val document = OnlineDatingProfile(
      Id("id"),
      Author("author"),
      Text("text"),
      Male,
      Female,
      19,
      None
    )
    val ageRule = new AgeRule()
    ageRule.exec(document) shouldBe Right(document)
  }

  it should "return the ModerationResult with KO and Underage status" in {
    val document = OnlineDatingProfile(
      Id("id"),
      Author("author"),
      Text("text"),
      Male,
      Female,
      17,
      None
    )
    val ageRule = new AgeRule()
    ageRule.exec(document) shouldBe Left(
      ModerationResult(document.id, KO, Some(Underage))
    )
  }

  it should "skip the document if it does not support age" in {
    val document = PrivateMessage(
      Id("id"),
      Author("author"),
      Text("text"),
      "toto",
      None
    )
    val ageRule = new AgeRule()
    ageRule.exec(document) shouldBe Right(document)
  }
}
