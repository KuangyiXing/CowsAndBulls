package cowsandbulls

import scala.io.StdIn

class App(secret: Secret) {

  private val game = new Game(secret)

  def play(userInput: => Stream[String]): Stream[Result] = {
    val guesses: Stream[Secret] = userInput.map(input => {
      val secret = input.toCharArray.map(_.asDigit).toVector
      Secret(secret)
    })
    game.play(guesses)
  }

}

object App {

  def main(args: Array[String]): Unit = {
    val app = new App(Secret.random())

    val userInput = Stream.continually(StdIn.readLine())

    app.play(userInput).foreach(println)
  }

}
