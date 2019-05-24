package cowsandbulls

import cats.effect.{ExitCode, IO, IOApp}

import scala.io.StdIn
import scala.util.Random

object Foo  {
//object Foo  extends IOApp {


  def run(args: List[String]): IO[ExitCode] = {

    val myReadln: IO[String] = IO {
      StdIn.readLine()
    }

    def myPrintln(msg: String): IO[Unit] = IO {
      println(msg)
    }

    val randomNum: IO[Int] = IO.apply {
      Random.nextInt()
    }

    val result: IO[Unit] = for {
      line1 <- myReadln
      line2 <- randomNum
      ioResult <- myPrintln(s"your lines were $line1 and $line2")
    } yield ioResult


    IO{ExitCode.Success}

    IO

    result.map(_ => {println("Made it"); ExitCode.Success})
  }
}
