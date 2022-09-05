package com.rockthejvm.part4pm

object BracelessSyntax extends App{

  val anIfExpression = if (2>3) "bigger" else "smaller"

  val anIfJavaExpression = if (2>3) {
    "bigger"
  } else {
    "smaller"
  }

  val scalaStype =
    if (2>3) "bigger"
    else "smaller"

  val pythonScala =
    if 2 > 3 then
      "bigger"
    else
      "smaller"

  val anIfExpression_5 =
    if 2 > 3 then
      val result = "bigger"
      result
    else
      val result = "smaller"
      result

  println(anIfExpression_5)

  val aForComprehension = for {
    n <- List(1,2,3)
    s <- List("black", "white")
  } yield s"$n$s"

  val another =
    for
      n <- List(1, 2, 3)
      s <- List("black", "white")
    yield s"$n$s"

  def computeMeaningOfLine(arg:Int): Int = {
    val partialResult = 40
    partialResult + 2
  }

  def computeMeaningOfLine2(arg: Int): Int =
    val partialResult = 40
    partialResult + 2

  class Animal {
    def eat:Unit =
      println(" I am eating")
  }

  class Animal2:

    def eat: Unit =
      println(" I am eating")
    end eat

    def grow: Unit =
      println(" I am growing")
    end grow

  end Animal2

}
