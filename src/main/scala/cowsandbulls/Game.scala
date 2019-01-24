package cowsandbulls

case class Game(secret: Secret) {

  def play(guesses: => Stream[Guess]): Stream[Result] = {
    guesses
      .map(guess => {
        secret.evaluateGuess(guess)
      })
      .takeWhile(_.bullNum != 4)
      .append(Stream(Result(cowNum = 0, bullNum = 4)))
  }
}
