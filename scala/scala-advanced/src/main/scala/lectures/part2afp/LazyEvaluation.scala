package lectures.part2afp

object LazyEvaluation extends App {
  lazy val y: Int = {
    println("hello")
    42
  }
  println(y);
  println(y)

  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }
  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no")

  def byNameMethod(n:Int):Int = {
    lazy val t = n
    t + t + t + 1
  }
  def retrieveMagicValue: Int = {
    println("Waiting")
    Thread.sleep(1000)
    42
  }
  println(byNameMethod(retrieveMagicValue))

  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1,25,40,5,25,23)
  val  lt23 = numbers.filter(lessThan30)
  val gt20 = lt23.filter(greaterThan20)

  val lt30lazy = numbers.withFilter(lessThan30)
  val gt20lazy = lt30lazy.withFilter(greaterThan20)
  println(gt20lazy)
  gt20lazy.foreach(println)

  for {
    a <- List(1,2,3) if a % 2 == 0
  } yield a + 1
  List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1)

}

abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B]
  def ++[B >: A](anotherStream: MyStream[B]): MyStream[B]

  def foreach(f: A => Unit):Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A]
  def takeAsLit(n: Int): List[A]
}
object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] = ???
}

object TestMyStream extends App {
  MyStream.from(1)(x => x + 1)
}













