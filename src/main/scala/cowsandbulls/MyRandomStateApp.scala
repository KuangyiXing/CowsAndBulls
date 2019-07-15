package cowsandbulls

import cats.Eval
import cats.data.{IndexedStateT, State}

object MyRandomStateApp {

  type MyRandomState[A] = State[MyRandom, A]

  def nextInt: MyRandomState[Int] = State[MyRandom, Int]{ rand: MyRandom =>
    rand.nextInt
  }

  def main(args: Array[String]): Unit = {
    val initialSeed = Console.readLong()

    val abc: MyRandomState[Int] = nextInt

    val foo: MyRandomState[String] = for {
      a <- abc
      b <- abc
    } yield s"my ints are $a and $b"

    val result = foo.run(new MyRandom(initialSeed)).value

    println(result)
  }
}
