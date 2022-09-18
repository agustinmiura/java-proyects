package part1recap

import scala.concurrent.Future

object AdvancedRecap extends App {

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 65
    case 5  => 999
  }

  val pf = (x: Int) => x match {
    case 1 => 42
    case 2 => 65
    case 5 => 999
  }

  val aFunction: (Int => Int) = aPartialFunction

  val modifiedList = List(1,2,3).map({
    case 1 => 42
    case _ => 0
  })

  val lifted = aPartialFunction.lift
  lifted(5000)

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 60 => 9000
  }

  type ReceivedFunction = PartialFunction[Any, Unit]

  def receive: ReceivedFunction = {
    case 1 => println("hello")
    case _ => println("Confused ...")
  }

  implicit val timeout = 3000
  def  setTimeout(f: () => Unit)(implicit timeout: Int) = ???
  setTimeout(()=> println("timeout"))

  case class Person(name: String) {
    def greet = s"Hi my name is $name"
  }
  implicit def fromStringToPerson(string: String):Person = (Person(string))
  "Name".greet

  implicit class Dog(name: String) {
    def bark = println("bark!!!!")
  }
  "Lassie".bark

  implicit val inverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  List(1,2, 3).sorted

  import scala.concurrent.ExecutionContext.Implicits.global
  val future = Future {
    println("Hello future")
  }

}
