package cowsandbulls

import java.util.concurrent.TimeUnit

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object Baa extends App {
  implicit val ec = ExecutionContext.global

  val MyConstant: Future[Int] = Future {
    fooooo()
  }

  val abc: Future[Int] = MyConstant
  println("the current", abc)

  val dfe: Future[Int] = MyConstant.flatMap(_ => MyConstant)
  println("the current", dfe)



  def fooooo(): Int = {
    Thread.sleep(1000)
    println("hello")
    43
  }

  println("booo")

  private val result: Future[Int] = dfe.map(_ + 1)

  println("apple")

  val sdfsf: Unit = result.onComplete((x: Try[Int]) => x match {
      case Success(v) => println(v)
      case Failure(t) => println(t)
    })

  println("almost finished")

  Thread.sleep(5000)

  println("the final", abc)
  println("the final", result)
}
