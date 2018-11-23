package cowsandbulls

case class Secret(value: Vector[Int]) {
  def evaluateGuess(guess: Secret): Result = {
    val bullCount = value.zip(guess.value).filter(tuple => tuple._1 == tuple._2).length
    val totalMatchCount = value.intersect(guess.value).length
    Result(
      bullNum = bullCount,
      cowNum = totalMatchCount - bullCount
    )
  }
}

object Secret {
  def apply(values:Int*): Secret = Secret(values.toVector)
}
