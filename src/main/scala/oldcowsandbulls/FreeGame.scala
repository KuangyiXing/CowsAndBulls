package oldcowsandbulls

import cats.arrow.FunctionK
import cats.effect.IO
import cats.free.Free
import cats.free.Free.liftF
import cats.{Id, ~>}
import com.sun.tools.javac.resources.compiler

import scala.io.StdIn

sealed trait Console[A]
case class Write(x: String) extends Console[Unit]
case object Read extends Console[String]

object FreeGame {
  type Script[A] = Free[Console, A]

  def write(x: String): Script[Unit] = liftF[Console, Unit](Write(x))

  val read: Script[String] = liftF[Console, String](Read)

  val compiler: FunctionK[Console, IO] = new FunctionK[Console, IO] {
    override def apply[A](fa: Console[A]): IO[A] = fa match {
      case Write(sth) => IO {println(sth)}
      case Read => IO {StdIn.readLine}
    }
  }

  val interpreter: FunctionK[Console, Id] = new FunctionK[Console, Id] {
    override def apply[A](fa: Console[A]): Id[A] = fa match {
      case Write(sth) => println(sth)
      case Read => StdIn.readLine()
    }
  }
//  val compiler: Console ~> IO = ???

  val game: Script[Unit] = for {
    _ <- write("what is your name")
    name <- read
    _ <- write(s"Hello $name")
  } yield ()


  def main(args: Array[String]): Unit = {
    //    Read.flatMap(x => Write(s"hello ${x}"))

    val result: Free[Console, String] = for {
      _ <- write("what is your name")
      name <- read
      _ <- write(s"Hello $name")
    } yield name

//    val x: IO[String] = result.foldMap(compiler)
//
//    x.unsafeRunSync()

    result.foldMap(interpreter)


  }


}







