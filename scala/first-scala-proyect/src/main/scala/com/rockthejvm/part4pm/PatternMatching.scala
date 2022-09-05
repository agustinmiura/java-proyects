package com.rockthejvm.part4pm

import scala.util.Random

object PatternMatching extends App {

  val random = new Random
  val x = random.nextInt(10)

  val description = x match {
    case 1 => "One"
    case 2 => "Two"
    case 3 => "Three"
    case 4 => "Four"
    case _ => "Something else"
  }

  println(x)
  println(description)

  case class Person(name:String, age:Int)
  val bob = Person("Bob", 20)

  val greeting = bob match {
    case Person(n, a) if (a < 21) => s"Hi my name is $n and I am $a years old "
    case Person(n, a) => s" More than 21 years  "
    case _ => 42
  }

  println(greeting)

  sealed class Animal
  case class Dog(breed: String) extends Animal
  case class Parrot(greeting: String) extends Animal

  val animal:Animal = Dog("Terra Nova")
  animal match {
    case Dog(someBreed) => println(s"This is a dog of the breed $someBreed")
    case _ => println("Another animal")
  }

  trait MyExpression
  case class Number(n:Int) extends MyExpression
  case class Sum(e1: MyExpression, e2: MyExpression) extends MyExpression
  case class Prod(e1: MyExpression, e2:MyExpression) extends  MyExpression

  val number2 = Number(2)
  val number3 = Number(3)

  def showExpression(expression: MyExpression): Int = {
    expression match {
      case Sum(e1, e2) => (showExpression(e1) + showExpression(e2))
      case Prod(e1, e2) => (showExpression(e1) * showExpression(e2))
      case Number(value) => value
    }
  }

  val addResult = showExpression(Sum(number2, number3))
  val prodResult = showExpression(Prod(number3, number2))
  println(prodResult)

  

}
