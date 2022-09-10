package lectures.part4implicits

object ImplicitIntro extends App {
  val pair = "Daniel" -> "555"
  val intPaiar = 1 -> 2

  case class Person(name: String) {
    def greet = s"Hi my name is $name"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Peter".greet)

  
}
