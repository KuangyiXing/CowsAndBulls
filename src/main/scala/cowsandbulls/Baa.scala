package cowsandbulls

import scala.util.Random

object Baa extends App {
  val myRandom = new Random(0)

  println(myRandom.nextInt())
  println(myRandom.nextInt())
}
