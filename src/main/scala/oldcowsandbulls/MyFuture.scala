package oldcowsandbulls

class MyFuture[A](myFun: () => A) {
  @volatile private var result: Option[A] = None

  private val thread: Thread = new Thread() {
    override def run(): Unit = {
      result = Some(myFun())
    }
  }

  thread.start()

  final def resultWhenDone(): A = {
    thread.join()
    result.get
  }

  def map[B](m: A => B): MyFuture[B] = new MyFuture[B](() => m(resultWhenDone()))

  def flatMap[B](m: A => MyFuture[B]) = new MyFuture[B](() => m(resultWhenDone()).resultWhenDone())
}

object MyFuture {
  def main(args: Array[String]): Unit = {
    val x: MyFuture[Int] = new MyFuture(() => {
      Thread.sleep(1000)
      println("hello")
      22
    })

    println("blah")

//    println(x.flatMap(_ => x).myWait())
    //TODO: Workout why hello is printed twice
    def foo(a: Int): MyFuture[Int] = new MyFuture[Int](() => a + 1)

    val y: MyFuture[Int] = x.flatMap(foo)
    println(y.resultWhenDone())
    println(y.resultWhenDone())
    println(y.resultWhenDone())

  }
}
