package cowsandbulls

import org.specs2.mutable.Specification

class GameSpec extends Specification {
  "Game" should {
    val secret = Secret(5, 6, 7, 0)
    val game = Game(secret)
    "ignore the guess after the correct guess" in {
      val guesses = Stream[Secret](
        Secret(1, 2, 3, 4),
        secret,
        Secret(1, 1, 1, 1)
      )

      val results: Stream[Result] = game.play(guesses)

      results must beEqualTo(
        Stream(
          Result(0, 0),
          Result(0, 4)
        )
      )
    }
  }
}
