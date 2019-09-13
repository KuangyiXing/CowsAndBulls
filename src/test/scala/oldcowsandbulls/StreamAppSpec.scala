package oldcowsandbulls

import org.specs2.mutable.Specification

class StreamAppSpec extends Specification {
  "App" should {
    val userInput: Stream[String] = Stream(
      "1234",
      "2345"
    )
    val secret = Secret(2,3,4,5)
    val game = new StreamApp(secret)
    "return stream of results" in {
      val results = game.play(userInput)
      val expectedResults = Stream(
        Result(3, 0),
        Result(0, 4)
      )
      results must beEqualTo(expectedResults)
    }
  }
}
