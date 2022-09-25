package part1recap


import scala.concurrent.Future
import scala.util.{Failure, Success}

object ScalaRecap extends App {

  val aCondition: Boolean = false
  def myFunction(x: Int) = {
    if (x>4) 42 else 65
  }
  class Animal
  trait Carnivore {
    val attributeA = ""
    def eat(a:Animal): Unit
  }
  object Carnivore {}
  abstract class MyList[+A]
  1 + 2
  1.+(2)
  val anIncrementer: Int => Int = (x:Int) => x + 1
  List(1,2,3).map(anIncrementer)
  println(List(1,2,3).map(_ + 1))

  val unknown: Any = 2
  try {
    throw new RuntimeException
  } catch {
    case e: Exception => println("I caught one")
  }

  import scala.concurrent.ExecutionContext.Implicits.global
  val future=  Future {
    42
  }
  future.onComplete {
    case Success(value) => println(s" I found the meaning of life $value")
    case Failure(exception) => println(s"I found $exception while searching for the meaning of life!")
  }
  val partialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 65
    case _ => 99
  }

  type AkkaReceive = PartialFunction[Any, Unit]
  def receive: AkkaReceive = {
    case 1 => 42
    case 2 => 65
    case _ => 99
  }

  implicit val timeout = 300
  def setTimeout(f: ()  => Unit)(implicit timeout: Int) = f()
  setTimeout(() => println("timeout"))(timeout)

  implicit class Dog(name: String) {
    def bark = println("Bark!")
  }
  "Lassie".bark

  implicit val numberOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  List(1,2,3).sorted

  case class Person(name: String) {
  }

}
