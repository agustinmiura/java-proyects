package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {

  def apply(element: A):Boolean = contains(element)

  def contains(element: A):Boolean
  def +(element:A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def forEach(f: A => Unit): Unit

}

class EmptySet[A] extends MySet[A] {

  def contains(element: A): Boolean = false
  def +(element: A): MySet[A] = new NonEmptySet[A](element, this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = this
  def forEach(f: A => Unit): Unit = ()
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  def contains(element: A): Boolean =
    element == head || tail.contains(head)

  def +(element: A): MySet[A] =
    if (this contains element) this
    else new NonEmptySet[A](element, this)

  def ++(anotherSet: MySet[A]): MySet[A] =
    tail ++ anotherSet + head

  def map[B](f: A => B): MySet[B] = (tail map f) + f(head)
  def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)
  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }
  def forEach(f: A => Unit): Unit = {
    f(head)
    tail forEach  f
  }

  object NonEmptySet {
    def apply[A](values: A*): MySet[A] = {
      @tailrec
      def buildSet(values: Seq[A], acumulator: MySet[A]): MySet[A] =
        if (values.isEmpty) acumulator
        else buildSet(values.tail, acumulator + values.head)
      buildSet(values.toSeq, new EmptySet[A])
    }
  }

  object MySet {
    /*
      val s = MySet(1,2,3) = buildSet(seq(1,2,3), [])
      = buildSet(seq(2,3), [] + 1)
      = buildSet(seq(3), [1] + 2)
      = buildSet(seq(), [1, 2] + 3)
      = [1,2,3]
     */
    def apply[A](values: A*): MySet[A] = {
      @tailrec
      def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
        if (valSeq.isEmpty) acc
        else buildSet(valSeq.tail, acc + valSeq.head)

      buildSet(values, new EmptySet[A])
    }
  }

}
