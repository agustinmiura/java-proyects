package lectures.part5ts

import scala.concurrent.Future

object HigherKindedTypes extends App {

  trait AHigherKindedType[F[_]]

  trait MyList[T] {
    def flatMap[B](f: T => B): MyList[B]
  }

  trait MyOption[T] {
    def flatMap[B](f: T => B): MyOption[B]
  }

  trait Myfuture[T] {
    def flatMap[B](f: T => B): Myfuture[B]
  }

  def multiply[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  trait Monad[F[_], A] {
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  implicit class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  implicit class MonadOption[A](option: Option[A]) extends Monad[Option, A] {
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
    override def map[B](f: A => B): Option[B] = option.map(f)
  }


  def multiply[F[_], A, B](implicit ma: Monad[F,A], mb: Monad[F, B]): F[(A,B)] = {
    for {
      a <- ma
      b <- mb
    } yield(a,b)
  }

  val monadList = new MonadList(List(1,2,3))
  monadList.flatMap(x => List(x, x + 1))
  monadList.map(_ * 2)

  println(multiply((List(1,2)), (List("a", "b"))))
  println(multiply(Some(2), Some("scala")))

}
