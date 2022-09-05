package com.rockthejvm.part4pm

object PatternsEverywhere extends App {

  val list = List(1,2,3,4)
  val evenOnes = for {
    x <- list if x % 2 == 0
  } yield 10 * x

  val tuples = List((1,2), (3,4))
  val filterTuples = for {
    (first, second) <- tuples
  } yield first * second

  val tuple = (1,2,3)
  val (a, b, c) = tuple
  println(s"I see the values $a, $b, $c")

  val head :: tail = list
  println(head)
  println(list)

  val mappedList = list.map {
    case v if v % 2 == 0 => v + "even"
    case 1 => "The one"
    case _ => "Something else"
  }

  println(mappedList)

  
}
