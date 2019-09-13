package oldcowsandbulls

import cats.effect.{ExitCode, IO, IOApp}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.Random

object Foo  {
//object Foo  extends IOApp {


  @tailrec
  def contains(x: Int, ints: List[Int]): Boolean = {
    ints match {
      case Nil => false
      case head :: _ if head == x => true
      case _ :: tail => contains(x, tail)
    }

  }

  @tailrec
  def sum(x: Int, ints: List[Int]): Int = {
    ints match {
      case Nil => x
      case head :: tail => sum(x + head, tail)
    }
  }

  def main(args: Array[String]): Unit = {
    println(contains(4, List(2,6,4,3)))
    println(sum(0, List(2,6,4,3)))
  }
}
