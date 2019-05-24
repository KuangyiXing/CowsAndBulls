package cowsandbulls

class MyIO[A](myFun: () => A) {
  def run: A = myFun()

  def pure(x: A): MyIO[A] = new MyIO(() => x)

  def map[B](m: A => B): MyIO[B] = new MyIO(() => m(myFun()))

  def flatMap[B](m: A => MyIO[B]): MyIO[B] = {
    new MyIO(() => m(myFun()).run)
  }

}

object MyIO {
  def main(args: Array[String]): Unit = {
    val x = new MyIO(() => println("hello"))
    val y = x.flatMap(_ => x)
    y.run

//    def hhh = () => println("hello")
//    hhh(hhh())
//    y.run
  }
}
