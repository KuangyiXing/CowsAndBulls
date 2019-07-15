package cowsandbulls

import cats.Monad
import cats.effect.IO
import cats.implicits._

import scala.io.StdIn

case class TaglessGame[F[_]: Monad](
  secret: Secret,
  inputLine: F[Option[String]],
  output: (String) => F[Unit]
) {

  private val InputGuess: F[Option[Guess]] = inputLine.map(_.map(input => Secret(input)))

  def play(): F[Unit] = {
    InputGuess.flatMap {
      case Some(guess) => {
        val result: Result = secret.evaluateGuess(guess)
        val successIO: F[Unit] = output("You win")
        val outIO: F[Unit] = output(s"Cows: ${result.cowNum}, Bulls: ${result.bullNum}")
        if (result.bullNum == 4) {
          successIO
        } else {
          outIO.flatMap { _ =>
            play()
          }
        }
      }

      case None => Monad[F].pure(())

    }
  }
}

object TaglessGame {
  def main(args: Array[String]): Unit = {
    val taglessGame: TaglessGame[IO] = TaglessGame(
      Secret.random(),
      IO { Option.apply(StdIn.readLine())},
      s => IO {println(s)}
    )

    taglessGame.play().unsafeRunSync()
  }
}
