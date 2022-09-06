package lectures.part1as

import scala.annotation.tailrec

object Recap extends App {

  val aCondition: Boolean = false
  val aConditionVal = if (aCondition) 42 else 65

  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  println(aCondition)

  val theUnit = println("Hello, Scala")

  def aFunction(x: Int): Int = x + 1

  @tailrec def factorial(n:Int, acum: Int): Int =
    if (n<=0) acum
    else factorial(n-1, n * acum)

  class Animal
  class Dog extends Animal
  val aDog:Animal = new Dog

  trait Carnivore {
    def eat(a:Animal): Unit
  }

  class Crocodrile extends Animal with Carnivore {
    override def eat(a:Animal): Unit = println("Crunch!")
  }

  val aCroc = new Crocodrile
  aCroc.eat(aDog)
  aCroc eat aDog

  val aCarnivore = new Carnivore {
    override def eat(a:Animal): Unit = println("Roar!")
  }

  abstract class MyList[+A]

  object MyList

  case class Person(val name: String,val age:Int)

  val throwsException = throw new RuntimeException

  val aPotentialFailire = try {
    throw new RuntimeException("")
  } catch {
    case e:Exception => " I caught an exception "
   } finally {
    println("Some logs")
  }

  val incrementer = (x:Int) => x + 1
  List(1,2,3).map(incrementer)
  List(1,2,3).map(_ + 1)

  val pairs = for {
    num <- List(1,2,3)
    char <- List('a', 'b', 'c')
  } yield num + "-" + char

  val aMap = Map(
    "Daniel" -> 789,
    "Tom" -> 999
  )

  val anOption = Some(2)

  val x = 2

  val order = x match {
    case 1 => "First"
    case 2 => "Second"
    case _ => "Another"
  }

  val bob = Person("name",22)
  val greeting = bob match {
    case Person(n,_) => s" Hello my name is $n "
  }

}
