package com.rockthejvm.part4pm

object AllThePatterns extends App {

  val x:Any = "Scala"
  val constants = x match {
    case 1 => "a number"
    case "Scala" => "Scala string"
    case true => "The boolean"
    case AllThePatterns => " Singleton object "
  }

  val matchAnything = x match {
    case _ =>
  }

  val matchAVariable = x match {
    case something => s"I ve found $something"
  }

  val aTuple = (1,2)
  val matchATuple = aTuple match {
    case (1,1) =>
    case (something, 2) => s" I've found $something "

  }

  val nestedTuple = (1, (2,3))
  val matchANestedTuple = nestedTuple match {
    case (_, (2, v)) =>
  }

  val list = List(1,2,3,42)
  val standardListMatching = list match {
    case List(1, _, _, _) => println("List extractor")
    case List(1, _*) => //list of arbitrary length
    case 1 :: List(_) =>
    case List(1,2,3) :+ 42 =>
  }

  val unknown: Any = 2

  val numbers = List(1,2,3)
  val numbersMatch = numbers match {
    case listOfStrings: List[String] => " a list of strings"
    case listOfNumbers: List[Int] => " A list of numbers"
    case _ => ""
  }

  println(numbersMatch)

}
