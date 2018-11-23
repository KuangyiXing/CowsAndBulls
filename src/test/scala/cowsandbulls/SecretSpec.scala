package cowsandbulls

import org.specs2.mutable.Specification

import scala.collection.mutable

class SecretSpec extends Specification {
  "Secret#evaluateGuess" should {
    "when nothing in common, it returns 0 bull and 0 cow" in {
      val secret = Secret(1, 2, 3, 4)
      val guess = Secret(5, 6, 7, 8)

      val expectedResult = Result(0, 0)

      secret.evaluateGuess(guess) must beEqualTo(expectedResult)
    }

    "when there are numbers in common" should {
      "when all of x number is in the matching position, it returns x bull and 0 cow" in {
        val secret = Secret(1, 2, 3, 4)
        val guess = Secret(1, 5, 3, 7)

        val expectedResult = Result(cowNum = 0, bullNum = 2)

        secret.evaluateGuess(guess) must beEqualTo(expectedResult)
      }

      "when none of x number is in matching position, it returns 0 bull and x cows" in {
        val secret = Secret(1, 2, 3, 4)
        val guess = Secret(2, 1, 5, 6)

        val expectedResult = Result(cowNum = 2, bullNum = 0)

        secret.evaluateGuess(guess) must beEqualTo(expectedResult)
      }

      "when there is x number in matching position and y number in different positions, it returns x bulls and y cows" in {
        val secret = Secret(3, 4, 7, 9)
        val guess = Secret(4, 3, 7, 8)

        val expectedResult = Result(cowNum = 2, bullNum = 1)
        secret.evaluateGuess(guess) must beEqualTo(expectedResult)
      }
    }
  }
}
