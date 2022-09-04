import scala.annotation.tailrec

abstract class MyList[+A] {
  def head:A
  def tail:MyList[A]
  def isEmpty:Boolean
  def add[B >: A](value:B):MyList[B]
  def printElements: String
  override def toString: String = "[" + printElements + "]"

  def map[B] (transformer: MyTransformer[A,B]): MyList[B]
  def flatMap[B](transformer: MyTransformer[A, MyList[B]]): MyList[B]
  def filter(predicate: MyPredicte[A]): MyList[A]

  def ++[B >: A](list: MyList[B]): MyList[B]

}

object Empty extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException()
  def tail: MyList[Nothing] = throw new NoSuchElementException()
  def isEmpty: Boolean = true
  def add[B >: Nothing](value: B): MyList[B] = new Cons(value, Empty)
  def printElements = ""

  def map[B](transformer: MyTransformer[Nothing, B]): MyList[B] = Empty
  def flatMap[B](transformer: MyTransformer[Nothing, MyList[B]]): MyList[B] = Empty
  def filter(predicate: MyPredicte[Nothing]): MyList[Nothing] = Empty

  def ++[B >: Nothing](list: MyList[B]) : MyList[B] = list
}

class Cons[+A](h:A, t:MyList[A]) extends MyList[A] {
  def head: A = h
  def tail: MyList[A] = t
  def isEmpty: Boolean = false
  def add[B >: A](value:B):MyList[B] = new Cons(value, this)
  def printElements: String =
    if (t.isEmpty) "" + h
    else h.toString + " " + t.printElements

  def map[B](transformer: MyTransformer[A, B]): MyList[B] = {
    new Cons(transformer.transform(h), t.map(transformer))

  }

  def filter(predicate: MyPredicte[A]): MyList[A] = {
    if (predicate.test(h)) new Cons(h, t.filter(predicate))
    else t.filter(predicate)
  }

  def ++[B >: A](list: MyList[B]) : MyList[B] = new Cons(h, t ++ list)

  def flatMap[B](transformer: MyTransformer[A, MyList[B]]): MyList[B] =
    transformer.transform(h) ++ t.flatMap(transformer)

}

trait MyPredicte[-T] {
  def test(elem:T): Boolean
}

trait MyTransformer[-A, B] {
  def transform(eleme: A):B
}

object ListTest extends App {
  val listOfIntegers: MyList[Int] = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(listOfIntegers.toString)

  println(listOfIntegers.map(new MyTransformer[Int,Int] {
    override def transform(eleme: Int): Int = eleme * 2
  })).toString

  println(listOfIntegers.filter(new MyPredicte[Int] {
    override def test(elem: Int): Boolean = elem % 2 == 0
  })).toString

  val anotherList: MyList[Int] = new Cons(4, new Cons(5, new Cons(6, Empty)))
  println((listOfIntegers ++ anotherList).toString)
  println(listOfIntegers.flatMap(new MyTransformer[Int, MyList[Int]] {
    override def transform(elem: Int): MyList[Int] = new Cons(elem, new Cons(elem +1, Empty))
  }))

}