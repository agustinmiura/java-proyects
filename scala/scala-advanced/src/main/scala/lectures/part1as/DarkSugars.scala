package lectures.part1as

import scala.util.Try

object DarkSugars extends App {

  def singleArgumentMethod(arg:Int): String = s"$arg little ducks"
  val description = singleArgumentMethod {
    10
  }

  val aTry = Try {
    throw new RuntimeException
  }

  List(1,2,3).map { x =>
    x + 1
  }

  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x:Int): Int = x + 1
  }

  def aFunkyInstance: Action = (x: Int) => x + 1

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println(" Hello , Scala ")
  })

  val aSweeterThread = new Thread(() => println("HEllo scala"))

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a:Int): Unit
  }

  val anInstance2: AnAbstractType = (a:Int) => println("sweet")

  val prependedList = 2 :: List(3,4)

  val aList2 = 1 :: 2 :: 3 :: List(4,5)

  class MyStream[T] {
    def -->:(value: T):MyStream[T] = this //actual implementation
  }

  val myStream = 1 -->: 2 -->: 2 -->: new MyStream[Int]

  class LittlePet(name:String) {
    def `and then woof`(gossip: String):Unit = println(s"$name said $gossip")
  }

  val aDog = new LittlePet("Wof")
  aDog `and then woof` "Scala is so sweet"

  class Composite[A,B]
  val composite: Composite[Int, String] = ???
  val composite2: Int Composite String = ???

  class -->[A,B]
  val towards: Int --> String = ???

  val anArray = Array(1,2,3)
  anArray(2) = 7
  anArray.update(2,7)

  class Mutable {
    private var internalMember: Int = 0
    def member = internalMember
    def member_= (value: Int): Unit =
      internalMember = value
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42

  

}
