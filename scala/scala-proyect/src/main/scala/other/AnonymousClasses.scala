package ar.com.name
package other

object AnonymousClasses extends App {

  abstract class Animal {
    def eat: Unit
  }

  val funnyAnimal: Animal = new Animal {
    override def eat: Unit = println("ahahahahahaha")
  }

  class Person(name: String) {
    def sayHi: Unit = println(s" Hi my name is $name")
  }

  val jim = new Person("Jim") {
    override def sayHi: Unit = println(" Hi my name is Jim. How can I help you?")
  }

  println(funnyAnimal.getClass)

  trait MyPredicate[-T] {
    def test(t: T): Boolean
  }

  trait MyTransformer[-A, B] {
    def convert(element: A): B
  }


}
