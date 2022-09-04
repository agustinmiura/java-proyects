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
  def sort(compare: (A,A) => Int): MyList[A]

  def zipWith[B,C](list: MyList[B], zip:(A, B) => C): MyList[C]
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

  def sort(compare: (Nothing,Nothing) => Int): MyList[Nothing] = Empty

  def zipWith[B,C](list: MyList[B], zip:(Nothing, B) => C): MyList[C] = {
    if (!list.isEmpty) throw new RuntimeException(" Lists don't have the same length ")
    else Empty
  }
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

  def sort(compare: (A,A) => Int): MyList[A] = {
    def insert(x: A, sortedList: MyList[A]): MyList[A] =
      if (sortedList.isEmpty) new Cons(x, Empty)
      else if (compare(x, sortedList.head) <= 0) new Cons(x, sortedList)
      else new Cons(sortedList.head, insert(x, sortedList.tail))

    val sortedTail = t.sort(compare)
    insert(h, sortedTail)
  }
  def zipWith[B,C](list: MyList[B], zip:(A, B) => C): MyList[C] = {
    if (list.isEmpty) throw new RuntimeException(" Lists don't have the same lenght ")
    else new Cons(zip(h, list.head), t.zipWith(list.tail, zip))
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

  val anotherList2: MyList[Int] = new Cons(10, new Cons(9, new Cons(8, Empty)))

  println(anotherList2.sort((x,y) => y - x))

  println(listOfIntegers.zipWith(anotherList, _ + " " + _))
}