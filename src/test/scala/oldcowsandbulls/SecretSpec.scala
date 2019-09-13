package oldcowsandbulls

import org.scalacheck.{Arbitrary, Gen}
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

import scala.collection.mutable
import scala.util.Random

class SecretSpec extends Specification with ScalaCheck {

  case class ZZZ(x: String)

  val GenSecret: Gen[Secret] = for {
    x1 <- Gen.choose(0, 9)
    x2 <- Gen.choose(0, 9)
    x3 <- Gen.choose(0, 9)
    x4 <- Gen.choose(0, 9)
  } yield Secret(x1,x2,x3,x4)

  case class SecretPair(s1: Secret, s2: Secret)

  val GenNonOverlappingPair: Gen[SecretPair] = {
    for {
      s1: Secret <- GenSecret
      s2: Secret <- GenSecret.suchThat(s => s.value.intersect(s1.value).isEmpty)
    } yield SecretPair(s1, s2)
  }

  implicit val arbGenPair: Arbitrary[SecretPair] = Arbitrary(GenNonOverlappingPair)

  implicit val arbSecret: Arbitrary[Secret] = Arbitrary(GenSecret)
//  private implicit val arbSecret: Gen[Secret] = foo.map(s => Secret(Vector(s,s,s,s))

  "Secret#evaluateGuess" should {
    "when nothing in common, it returns 0 bull and 0 cow" in {
      prop { (pair: SecretPair) =>
        val result = pair.s1.evaluateGuess(pair.s2)
        result must beEqualTo(Result(0, 0))
      }
    }

      "total number of cows and bulls should be between 0 and 4" in {
      prop { (secret: Secret, guess: Secret) =>
        val result = secret.evaluateGuess(guess)
        val total: Int = result.bullNum + result.cowNum
        total must beBetween(0, 4)
      }
    }

    "swapping secret and guess should give us the same result" in {
      prop { (secret: Secret, guess: Secret) =>
        val result = secret.evaluateGuess(guess)
        val reversedResult = guess.evaluateGuess(secret)
        result must beEqualTo(reversedResult)
      }
    }

    "total matches will be independent of ordering" in {
      prop { (secret: Secret, guess: Secret) =>
        val shuffledGuess = Secret(Random.shuffle(guess.value))
        val result = secret.evaluateGuess(guess)
        val shuffledResult = secret.evaluateGuess(shuffledGuess)
        result.cowNum + result.bullNum must beEqualTo(shuffledResult.bullNum + shuffledResult.cowNum)
      }
    }

    "Evaluating self value should return 4 bulls" in {
      prop { (secret: Secret) =>
        val result = secret.evaluateGuess(secret)
        result must beEqualTo(Result(cowNum = 0, bullNum = 4))
      }
    }

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

      "when there are repetitive numbers" should {
        "each digit can only count towards the score once, and Bulls are counted before Cows" in {
          val secret = Secret(3, 3, 4, 5)
          val guess = Secret(3, 6, 3, 3)

          val expectedResult = Result(cowNum = 1, bullNum = 1)
          secret.evaluateGuess(guess) must beEqualTo(expectedResult)
        }
      }
    }
  }

  "Secret#random" should {
    "generate a secret with size of 4" in {
      val secret = Secret.random()
      secret.value.size must beEqualTo(4)
    }

    "generate a secret with all digits between 0 - 9" in {
      val secret = Secret.random()
      secret.value.forall(digit => digit <= 9 && digit >= 0) must beTrue
    }
  }

  "Secret#apply" should {
    "generate a secret" in {
      val secret = Secret("1234")
      val expectedSecret = Secret(1,2,3,4)
      secret must beEqualTo(expectedSecret)
    }
  }
}
