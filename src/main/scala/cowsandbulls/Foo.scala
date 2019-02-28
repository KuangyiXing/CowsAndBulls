package cowsandbulls

import cats.effect.IO

object Foo {

  def main(args: Array[String]): Unit = {
//    val foo: IO[Unit] = IO {
//      println("hello")
//    }

    //foo.unsafeRunSync()
    val x: IO[Int] = IO {
      println("hello")
      Console.readInt()
    }

//    val result: Int = x.unsafeRunSync()
//    println(result)

    val result: IO[Int] = blah(x, x)

    val actualResult: Int = result.unsafeRunSync()

    println(actualResult)
  }

  def blah(x: IO[Int], y: IO[Int]): IO[Int] = {
    x.flatMap(a => y.map(b => a + b))
  }
}
