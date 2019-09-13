package cowsandbulls

import cats.Id
import cats.data.State
import org.specs2.mutable.Specification

class GameSpec extends Specification {
  "Game" should {
    "should not blow up" in {
      val inputline: Id[Option[String]] = Some("1345")
      val outputline: String => Id[Unit] = _ => Unit
      val secret = Secret(1, 3, 4, 5)
      val game = Game(secret, inputline, outputline)

      game.play()
      ok("200")
    }

    "keep returning until there is a match" in {

      case class World(
        outputLines: Vector[String],
        remainingLines: Vector[String]
      )

      type WorldState[A] = State[World, A]

      val inputLine: WorldState[Option[String]] = State[World, Option[String]] { oldWorld =>
        (World(oldWorld.outputLines, oldWorld.remainingLines.tail), oldWorld.remainingLines.headOption)
      }

      def output(msg: String): WorldState[Unit] = State[World, Unit] { oldWorld =>
        (oldWorld.copy(outputLines = oldWorld.outputLines :+ msg), ())
      }


      val initalWorld: World = World(
        outputLines = Vector.empty,
        remainingLines = Vector(
        "1234",
        "1235",
        "1467"
        )
      )

      val coolGame = Game(
        Secret(1, 4, 6, 7),
        inputLine,
        output
      )

      val result: State[World, Unit] = coolGame.play()
      val (finalWorld, _) = result.run(initalWorld).value


      finalWorld.outputLines must beEqualTo(Vector(
        "Cows: 1, Bulls: 1",
        "Cows: 0, Bulls: 1",
        "You win"
      ))

    }

    "exit gracefully at EOF" in {

      case class World(
        outputLines: Vector[String],
        remainingLines: Vector[String]
      )

      type WorldState[A] = State[World, A]

      val inputLine: WorldState[Option[String]] = State[World, Option[String]] { oldWorld =>
        oldWorld.remainingLines match {
          case head +: tail => (World(oldWorld.outputLines, tail), Some(head))
          case _ => (World(oldWorld.outputLines, Vector.empty), None)
        }
      }

      def output(msg: String): WorldState[Unit] = State[World, Unit] { oldWorld =>
        (oldWorld.copy(outputLines = oldWorld.outputLines :+ msg), ())
      }


      val initalWorld: World = World(
        outputLines = Vector.empty,
        remainingLines = Vector(
        "1234",
        "1235"
        )
      )

      val coolGame = Game(
        Secret(1, 4, 6, 7),
        inputLine,
        output
      )

      val result: State[World, Unit] = coolGame.play()
      val (finalWorld, _) = result.run(initalWorld).value


      finalWorld.outputLines must beEqualTo(Vector(
        "Cows: 1, Bulls: 1",
        "Cows: 0, Bulls: 1"
      ))

    }

  }
}
