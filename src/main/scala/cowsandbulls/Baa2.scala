package cowsandbulls

import cats.effect.IO

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import cats.{Monad, Monoid}
import cats.implicits._

import scala.io.StdIn

object Baa2 {
  implicit val ec = ExecutionContext.global

  def main(args: Array[String]): Unit = {
//    foo.unsafeRunSync()
  }

  val read: IO[String] = IO {StdIn.readLine()}
  def write(x: String): IO[Unit] = IO {println(x)}

  def foo[M[_]: Monad](
    myRead: M[String],
    myWrite: (String) => M[Unit]
  ): M[Unit] = {
    for {
      l1 <- myRead
      l2 <- myRead
      _ <- myWrite(l1 + l2)
    } yield ()
  }

  foo(read, write)

  case class MyContainer[A](a: A)

  val read2: MyContainer[String] = MyContainer("fred")
  def write2(x: String): MyContainer[Unit] = MyContainer(())

  val containerM: Monad[MyContainer] = new Monad[MyContainer] {
    override def pure[A](x: A): MyContainer[A] = ???

    override def flatMap[A, B](fa: MyContainer[A])(f: A => MyContainer[B]): MyContainer[B] = ???

    override def tailRecM[A, B](a: A)(f: A => MyContainer[Either[A, B]]): MyContainer[B] = ???
  }

  foo(read2, write2)(containerM)
}
