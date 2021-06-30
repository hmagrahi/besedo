package rules

trait Rules[In, Out, Or] {
  val priority: Int
  def exec(document: In): Either[Or, Out]
}

object Rules {
  val BesedoPriorityBatch = List(
    new ResolveRule(),
    new PriceRule(),
    new UrlEmailRule(),
    new ConVowRule(),
    new AgeRule()
  ).sortBy(_.priority)
}
