package cowsandbulls

import scala.io.StdIn

class StreamApp(secret: Secret) {

  private val game = new Game(secret)

  def play(userInput: => Stream[String]): Stream[Result] = {
    val guesses: Stream[Secret] = userInput.map(input => {
      Secret(input)
    })
    game.play(guesses)
  }

}

//object StreamApp extends App {
object StreamApp {

    val app = new StreamApp(Secret.random())

    val userInput = Stream.continually(StdIn.readLine())

    app.play(userInput).foreach(println)

}
