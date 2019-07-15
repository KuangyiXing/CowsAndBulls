package cowsandbulls

case class MyRandom(seed: Long) {
  def nextInt : (MyRandom, Int) = {
    val nextSeed = seed * 2189371983244584389L % 3414142413567654439L
    (MyRandom(nextSeed), (nextSeed % Int.MaxValue).toInt)
  }

  def nextString: (MyRandom, String) = {
    val (nextRandom, _) = this.nextInt
    (nextRandom, nextRandom.seed.toString)
  }
}

object MyRandomFoo {
  def main(args: Array[String]): Unit = {

    val initialSeed = Console.readLong()

    val rand1 = MyRandom(initialSeed)
    val (rand2, x) = rand1.nextInt
    val (rand3, y) = rand2.nextInt
    val (_, z) = rand3.nextInt

    println(s"my random numbers: $x and $y and $z")
  }
}
