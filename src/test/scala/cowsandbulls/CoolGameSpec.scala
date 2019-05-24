package cowsandbulls

import cats.effect.IO
import org.specs2.mutable.Specification

class CoolGameSpec extends Specification {
  "CoolGame" should {
    "keep returning until there is a match" in {
      var remainingLines = Vector(
        "1234",
        "1235",
        "1467"
      )

      var outputLines: Vector[String] = Vector()

      val inputLine = IO[Option[String]] {
        val result = remainingLines.headOption
        remainingLines = remainingLines.tail
        result
      }

      def output(msg: String): IO[Unit] = IO {
        outputLines = outputLines :+ msg
      }

      val coolGame = CoolGame(
        Secret(1,4,6,7),
        inputLine,
        output
      )
      val result: IO[Unit] = coolGame.play()
      result.unsafeRunSync()

      outputLines must beEqualTo(Vector(
        "Cows: 1, Bulls: 1",
        "Cows: 0, Bulls: 1",
        "You win"
      ))
    }
  }

}
