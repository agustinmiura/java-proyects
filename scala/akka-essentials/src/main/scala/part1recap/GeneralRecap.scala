package part1recap

import scala.util.Try

object GeneralRecap extends App {

  println("Running")

  val aCondition: Boolean = false

  var aVariable = 99
  aVariable += 1

  val aConditionVal = if (aCondition) 42 else 65

  val aCodeBlock = {
    if (aCondition) 74
    66
  }

  val theUnit  = println("Hello scala")

  def aFunction(x:Int) = x + 1

  def factorial(x: Int, acum: Int): Int =
    if (x <= 0) acum
    else factorial(x-1, acum * x)

  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodrile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = ???
  }

  val aCroc = new Crocodrile
  aCroc eat aDog

  abstract class MyList[+A]

  object MyList

  case class Person(name: String, age: Int)

  val incrementor = new Function1[Int,Int] {
    def apply(v1: Int): Int = v1 + 1
  }
  val incremented = incrementor(1)
  val anonymousIncrementor = (x: Int) => x + 1
  List(1,2,3).map(incrementor)

  val pairs = for {
    num <- List(1,2,3,4)
    char <- List('a', 'b', 'c', 'd')
  } yield num + "-" + char

  val anOption = Some(2)
  val aTry = Try {
    throw new RuntimeException
  }

  val unknown = 2
  val order = unknown match {
    case 1 => "first"
    case 2 => "second"
    case _ => "unknown"
  }

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi my name is $n"
    case _ => "I don't know my name"
  }

  


}
