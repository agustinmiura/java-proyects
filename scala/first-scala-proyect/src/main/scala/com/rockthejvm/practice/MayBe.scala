package com.rockthejvm.practice

abstract class MayBe[+T] {
  def map[B](f: T => B): MayBe[B]
  def flatMap[B](f: T => MayBe[B]): MayBe[B]
  def filter(p: T => Boolean): MayBe[T]
}

case object MayBeNot extends MayBe[Nothing] {
  def map[B](f: Nothing => B): MayBe[B] = MayBeNot
  def flatMap[B](f: Nothing => MayBe[B]): MayBe[B] = MayBeNot
  def filter(p: Nothing => Boolean): MayBe[Nothing] = MayBeNot
}

case class Just[+T](value: T) extends MayBe[T] {
  def map[B](f: T => B): MayBe[B] = Just(f(value))
  def flatMap[B](f: T => MayBe[B]): MayBe[B] = f(value)
  def filter(p: T => Boolean): MayBe[T] =
    if (p(value)) this
    else MayBeNot
}

object MayBeTest extends App {
  val just3 = Just(3)
  println(just3)

  println(just3.map(_ * 3))
  println(just3.flatMap(x => Just(x % 2 ==0)))
  println(just3.filter(_ % 2 == 0))
}