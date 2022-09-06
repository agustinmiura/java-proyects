package lectures.part1as

import scala.Array.unapplySeq

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"The only element is $head")
    case _ => println("Nothing")
  }

  class Person(val name:String, val age:Int)

  object Person {
    def unapply(aPerson:Person): Option[(String, Int)] =
      if (aPerson.age<21) None
      else Some((aPerson.name, aPerson.age))
    def unapply(age: Int): Option[String] =
      Some(if(age<21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)

  val greeting = bob match {
    case Person(n,a) => s"Hi my name is $n and my age is $a"
  }

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }

  object even {
    def unapply(arg:Int): Boolean = arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg:Int): Boolean = arg > -10 && arg < 10
  }

  val n:Int = 8
  val mathProperty = n match {
    case singleDigit() => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }

  //infix patterns
  case class Or[A,B](a:A,b:B)

  object Or {

  }

  val myEither = Or(2, "Two")
  val humanDescription = myEither match {
    case number Or string => s"$number is written as $string"
  }
  println(humanDescription)
  println(mathProperty)

  val vararg = numbers match {
    case List(1, _*) => "starting with one"
    case _ => "anotherCode"
  }

  abstract class MyList[+A] {
    def head: A = ???

    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]

  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1, 2"
    case _ => "something else"
  }

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get = person.name
    }
  }

  println(bob match {
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "An alien"
  })

}
