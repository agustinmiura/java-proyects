package com.rockthejvm.part3fp

def toCurry(f: (Int, Int) => Int): (Int => Int => Int) =
  x => y => f(x,y)

def fromCurry(f: (Int => Int => Int)) : (Int, Int) => Int =
  (x,y) => f(x)(y)

def compose[A,B,T](f: A => B, g: T => A): T => B = x => f(g(x))

def andThen[A,B,C](f: A => B, g: B => C): A => C =
  x => g(f(x))

object HofAndCurriesExercise extends App {

  def superAdder: (Int => Int => Int) = toCurry(_ + _ )
  def add = superAdder(4)
  println(add(17))

  val simpleAdder = fromCurry(superAdder)
  println(simpleAdder(4,17))

  val add2 = (x: Int) => x + 2
  val times3 = (x: Int) => x * 3

  val composed = compose(add2, times3)

  val orderes = andThen(add2, times3)

  println(composed(4))

  println(orderes(4))

}
