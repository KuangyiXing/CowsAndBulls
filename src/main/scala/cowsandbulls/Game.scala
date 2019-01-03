package cowsandbulls

case class Game(secret: Secret) {
  def play(guesses: Stream[Secret]): Stream[Result] = guesses.map(guess => secret.evaluateGuess(guess))
}
