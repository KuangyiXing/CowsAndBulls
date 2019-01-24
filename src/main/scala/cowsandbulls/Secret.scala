package cowsandbulls

import scala.util.Random

case class Secret(value: Vector[Int]) {
  def evaluateGuess(guess: Guess): Result = {
    val bullCount = value.zip(guess.value).filter(tuple => tuple._1 == tuple._2).length
    val totalMatchCount = value.intersect(guess.value).length
    Result(
      bullNum = bullCount,
      cowNum = totalMatchCount - bullCount
    )
  }
}

object Secret {
  def apply(values: Int*): Secret = Secret(values.toVector)

  def random(): Secret = {
    val secretValue = Vector.fill(4)(Random.nextInt(10))
    Secret(secretValue)
  }
}
