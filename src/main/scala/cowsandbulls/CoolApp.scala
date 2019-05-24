package cowsandbulls

import cats.effect.IO

import scala.io.StdIn
import scala.util.Try

class CoolApp(
  secret: Secret,
  inputLine: IO[Option[String]],
  output: (String) => IO[Unit]
) {
  private val game = new CoolGame(
    secret,
    inputLine,
    output
  )

  def evaluate(): IO[Unit] = {
    game.play()
  }
}

object CoolApp extends App {

  val inputLine: IO[Option[String]] = IO {
    Option(StdIn.readLine())
  }

  def printMsg(msg: String): IO[Unit] = {
    IO[Unit](println(msg))
  }

  val app = new CoolApp(Secret.random(), inputLine, printMsg)

  private val result: IO[Either[Throwable, Unit]] = app.evaluate().attempt
  result.flatMap {
    case Left(error) =>
      printMsg(s"There was an error: ${error.getMessage}")
    case Right(_) => IO()
  }.unsafeRunSync()
}
