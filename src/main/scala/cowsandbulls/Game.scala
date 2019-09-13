package cowsandbulls

import cats.Monad
import cats.effect.IO
import cats.implicits._

import scala.io.StdIn

case class Game[F[_] : Monad](
  secret: Secret,
  inputLine: F[Option[String]],
  output: (String) => F[Unit]
) {

  private val InputGuess: F[Option[Guess]] = inputLine.map(_.map(input => Secret(input)))

  private val Continue = Left(())
  private val Terminate = Right(())

  def playOneTurn(x: Unit): F[Either[Unit, Unit]] = {
    output("Input a guess") *>
      InputGuess.flatMap {
        case Some(guess) => {
          val result: Result = secret.evaluateGuess(guess)
          if (result.bullNum == 4) {
            output("You win").map(_ => Terminate)
          } else {
            output(s"Cows: ${result.cowNum}, Bulls: ${result.bullNum}")
              .map(_ => Continue)
          }
        }
        case None => Monad[F].pure(Right(()))
      }
  }

  def play(): F[Unit] = {
    Monad[F].tailRecM(())(playOneTurn)
  }
}


object Game {
  def main(args: Array[String]): Unit = {
    val game: Game[IO] = Game(
      Secret.random(),
      IO {
        Option.apply(StdIn.readLine())
      },
      s => IO {
        println(s)
      }
    )

    game.play().unsafeRunSync()
  }
}
