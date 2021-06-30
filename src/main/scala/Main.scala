object Main {

  def main(args: Array[String]): Unit = {

    val path = "/home/hmagrahi/Desktop/besedo/src/main/resources/test.json"
    val batch = FlowRunner.readFile(path)
    val rs = FlowRunner.run(batch)
    FlowRunner.writeFile(rs)
  }
}
