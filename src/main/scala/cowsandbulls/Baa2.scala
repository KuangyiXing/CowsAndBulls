package cowsandbulls

import cats.effect.IO

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

import cats.Monoid

object Baa2 extends App {
  implicit val ec = ExecutionContext.global

  case class Property(title: String, bedRooms: Int)
  val x: List[Property] = List(
    Property("nice home", 3),
    Property("renovators delight", 2)
  )

  val propertyMonoid: Monoid[Property] = new Monoid[Property] {
    override def empty: Property = Property("", 0)

    override def combine(x: Property, y: Property): Property = Property(
      x.title + y.title,
      x.bedRooms + y.bedRooms
    )
  }

  private val sum: Property = Monoid.combineAll(x)(propertyMonoid)

  val MyConstant: IO[Int] = IO {
    fooooo()
  }

  val abc: IO[Int] = MyConstant


  println("the address of ", abc)

  val dfe: IO[Int] = MyConstant.flatMap(_ => MyConstant)

  println("the address of ", dfe)
  def fooooo(): Int = {
    Thread.sleep(1000)
    println("hello")
    43
  }

  println("booo")

  private val result: IO[Int] = dfe.map(_ + 1)

  println("apple")

  val sdfsf: IO[Unit] = result.attempt.map(x => x match {
      case Right(v) => println(v)
      case Left(t) => println(t)
    })

  sdfsf.unsafeRunSync()

  println("almost finished")

  Thread.sleep(5000)
}
