package com.rockthejvm.part3fp

object MapFlatMapFilterFor extends App {

  val numbers = List(1,2,3)
  println(numbers)

  println(numbers.head)
  println(numbers.tail)

  println(numbers.map(_ + 1))
  println(numbers.map( _ + " is a number"))

  println(numbers.filter(_ % 2 == 0))

  val toPair = (x:Int) => List(x, x+1)

  println(numbers.flatMap(toPair))

  val otherNumbers = List(1,2,3,4)
  val chars = List('a','b','c','d')
  val colors = List("Black", "White")

  val combinations = numbers.flatMap(n => chars.map(c => "" + c + n))
  val combinations2 = numbers.flatMap(n => chars.flatMap(c => colors.map(color => "_" + c + n + color)))

  println(combinations)
  println(combinations2)

}
