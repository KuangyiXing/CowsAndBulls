package oldcowsandbulls

import cats.effect.IO

case class CoolGame(
  secret: Secret,
  inputLine: IO[Option[String]],
  output: (String) => IO[Unit]
) {

  private val InputGuess: IO[Option[Guess]] = inputLine.map(_.map(input => Secret(input)))

  def play(): IO[Unit] = {
    InputGuess.flatMap {
      case Some(guess) => {
        val result: Result = secret.evaluateGuess(guess)
        val successIO: IO[Unit] = output("You win")
        val outIO: IO[Unit] = output(s"Cows: ${result.cowNum}, Bulls: ${result.bullNum}")
        if (result.bullNum == 4) {
          successIO
        } else {
          outIO.flatMap { _ =>
            play()
          }
        }
      }

      case None => IO{}

    }
  }
}
