package oldcowsandbulls

import cats.{Eval, Id}
import cats.arrow.FunctionK
import cats.data.State
import cats.effect.IO
import org.specs2.mutable.Specification

import scala.io.StdIn

class FreeGameTest extends Specification {

  var linesWritten: List[String] = List()
  var linesToBeRead: List[String] = List("Fred")

  val testIntrepeter: FunctionK[Console, Id] = new FunctionK[Console, Id] {
    override def apply[A](fa: Console[A]): Id[A] = fa match {
      case Write(x) => linesWritten = x :: linesWritten
      case Read => {
        val x = linesToBeRead.head
        linesToBeRead = linesToBeRead.tail
        x
      }
    }
  }

  "game should do something" in {
    FreeGame.game.foldMap(testIntrepeter)
    linesWritten should beEqualTo(List("Hello Fred", "what is your name"))
  }

  "the game should do something" in {
    case class World(
      outputLines: Vector[String],
      remainingInputs: Vector[String]
    )

    type WorldState[A] = State[World, A]

    val pureTestIntrepeter: FunctionK[Console, WorldState] = new FunctionK[Console, WorldState] {
      override def apply[A](fa: Console[A]): WorldState[A] = fa match {
        case Write(x) => State[World, Unit] {oldWorld =>
          (World(x +: oldWorld.outputLines, oldWorld.remainingInputs),())
        }
        case Read => State[World, String] {oldWorld =>
          (World(oldWorld.outputLines, oldWorld.remainingInputs.tail), oldWorld.remainingInputs.head)
        }
      }
    }
    val x: WorldState[Unit] = FreeGame.game.foldMap(pureTestIntrepeter)
    val result: (World, Unit) = x.run(World(Vector.empty, Vector("Fred"))).value
    result._1.outputLines must beEqualTo(Vector("Hello Fred", "what is your name"))
  }
  }
