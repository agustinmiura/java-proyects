package com.rockthejvm.practice

abstract class MyList[+A] {
  def head:A
  def tail:MyList[A]
  def isEmpty:Boolean
  def add[B >: A](value:B):MyList[B]
  def printElements: String
  override def toString: String = "[" + printElements + "]"

  def map[B] (transformer: A => B): MyList[B]
  def flatMap[B](transformer: A => MyList[B]): MyList[B]
  def filter(predicate: A => Boolean): MyList[A]

  def ++[B >: A](list: MyList[B]): MyList[B]

  def foreach(f: A => Unit): Unit
}

object Empty extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException()
  def tail: MyList[Nothing] = throw new NoSuchElementException()
  def isEmpty: Boolean = true
  def add[B >: Nothing](value: B): MyList[B] = new Cons(value, Empty)
  def printElements = ""

  def map[B](transformer: Nothing => B): MyList[B] = Empty
  def flatMap[B](transformer: Nothing =>  MyList[B]): MyList[B] = Empty
  def filter(predicate: Nothing => Boolean): MyList[Nothing] = Empty

  def ++[B >: Nothing](list: MyList[B]) : MyList[B] = list

  def foreach(f: Nothing => Unit): Unit = ()
}

class Cons[+A](h:A, t:MyList[A]) extends MyList[A] {
  def head: A = h
  def tail: MyList[A] = t
  def isEmpty: Boolean = false
  def add[B >: A](value:B):MyList[B] = new Cons(value, this)
  def printElements: String =
    if (t.isEmpty) "" + h
    else h.toString + " " + t.printElements

  def map[B](transformer: A => B): MyList[B] = {
    new Cons(transformer(h), t.map(transformer))
  }

  def filter(predicate: A => Boolean): MyList[A] = {
    if (predicate(h)) new Cons(h, t.filter(predicate))
    else t.filter(predicate)
  }

  def ++[B >: A](list: MyList[B]) : MyList[B] = new Cons(h, t ++ list)

  def flatMap[B](transformer: A => MyList[B]): MyList[B] =
    transformer(h) ++ t.flatMap(transformer)

  def foreach(f: A => Unit): Unit = {
    f(h)
    t.foreach(f)
  }

}

object ListTest extends App {

  val listOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(listOfIntegers.toString)

  println(listOfIntegers.map(_ * 2))

  val anotherList: MyList[Int] = new Cons(4, new Cons(5, new Cons(6, Empty)))
  println((listOfIntegers ++ anotherList).toString)
  println(listOfIntegers.flatMap(elem => new Cons(elem, new Cons(elem +1, Empty))).toString)

  listOfIntegers.foreach(println)

}