import io.circe.parser
import io.circe.syntax.EncoderOps
import models.Category.Miscellaneous
import models.Document.{Author, Id, Price, Text}
import models.Gender.{Female, Male}
import models.ModerationStatus.KO
import models.RejectionReason.Scam
import models._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class FlowRunnerSpec extends AnyFlatSpec with Matchers {
  "Runner flow" should "should Return ko if a batch non resolved" in {
    val batch = Batch(
      "id",
      content = List(
        OnlineDatingProfile(
          Id("id"),
          Author("author"),
          Text("text"),
          Male,
          Female,
          -1,
          None
        ).asJson
      )
    )

    FlowRunner.run(batch) shouldBe
      ModerateBatch(
        "id",
        List(ModerationResult(Id("id"), KO, None))
      )
  }

  it should "should Return Scam" in {
    val batch = Batch(
      "id",
      content = List(
        ClassifiedAds(
          Id("id"),
          Author("author"),
          Text("test"),
          Miscellaneous,
          Price(110f)
        ).asJson
      )
    )

    FlowRunner.run(batch) shouldBe
      ModerateBatch(
        "id",
        List(ModerationResult(Id("id"), KO, Some(Scam)))
      )
  }

  it should "should Return Error for malformed json" in {
    val batch = Batch(
      "id",
      content = List(
        parser
          .parse("""{
          |"id":"test",
          |"toto":"toto"
          |}""".stripMargin)
          .toOption
          .get,
        parser
          .parse("""{
      |"toto":"toto"
      |}""".stripMargin)
          .toOption
          .get
      )
    )

    FlowRunner.run(batch) shouldBe
      ModerateBatch(
        "id",
        List(
          ModerationResult(Id("test"), ModerationStatus.Error, None),
          ModerationResult(Id("NaN"), ModerationStatus.Error, None)
        )
      )
  }

  it should "should Return ko if a batch non resolved before Scam" in {
    val batch = Batch(
      "id",
      content = List(
        ClassifiedAds(
          Id("id"),
          Author("author"),
          Text(""),
          Miscellaneous,
          Price(100f)
        ).asJson
      )
    )

    FlowRunner.run(batch) shouldBe
      ModerateBatch(
        "id",
        List(ModerationResult(Id("id"), KO, None))
      )
  }

}
