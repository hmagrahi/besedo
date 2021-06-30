import java.io.{BufferedWriter, FileWriter}
import java.lang.reflect.MalformedParametersException
import java.nio.file.{Files, Paths}

import io.circe.syntax.EncoderOps
import io.circe.parser
import models.ModerationStatus.OK
import models.{Batch, ModerateBatch, ModerationResult}
import rules.{ParseRule, Rules}

object FlowRunner {
  def readFile(path: String): Batch = {
    val json = Files.readString(Paths.get(path))
    parser
      .parse(json)
      .flatMap(_.as[Batch])
      .getOrElse(
        throw new MalformedParametersException(
          "Please check your json path or it's content"
        )
      )
  }

  def writeFile(mb: ModerateBatch): Unit = {
    val value = mb.asJson
    val writer = new BufferedWriter(new FileWriter("output.json"))
    writer.write(value.dropNullValues.noSpaces)
    writer.close()
  }
  import scala.collection.parallel.CollectionConverters._

  def run(batch: Batch): ModerateBatch = {
    val parseRule = new ParseRule()
    val rs = batch.content.par.map { documentStr =>
      {
        (Rules.BesedoPriorityBatch
          .map(rule => {
            parseRule
              .exec(documentStr)
              .flatMap(mat => rule.exec(mat))
              .fold(
                err => Left(err),
                d => Right(ModerationResult(d.id, OK, None))
              )
          })
          .partitionMap(identity) match {
          case (ls, _) if ls.nonEmpty => ls.headOption
          case (_, rs)                => rs.headOption
        }).getOrElse(throw new RuntimeException("error in batch"))
      }
    }
    ModerateBatch(batch.id, rs.toList)
  }
}
