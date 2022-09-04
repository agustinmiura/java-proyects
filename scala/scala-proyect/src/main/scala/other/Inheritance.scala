package ar.com.name
package other

object Inheritance extends App {

  class Animal {
    val creatureType = "wild"

    def eat = println("nom nom nom")
  }

  class Cat extends Animal {
    def crunch = {
      eat
      println("Crunch crunch")
    }
  }

  val cat = new Cat
  cat.crunch

  class Person(name: String, age: Int) {
    def this(name: String) = this(name, 0)
  }

  class Adult(name: String, age: Int, idCard: String) extends Person(name)

  class Dog(val value: String) extends Animal {
    override val creatureType = value

    override def eat = {
      super.eat
      println("Crunch crunch")
    }
  }

  val dog = new Dog("K9")
  println(dog.creatureType)

  val another: Animal = new Dog("K9")
  another.eat

}
