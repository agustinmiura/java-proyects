package ar.com.name
package other

object AbstractDataTypes extends App {

  abstract class Animal {
    val createType: String

    def eat: Unit
  }

  class Dog extends Animal {
    override val createType: String = "Dog"

    override def eat = println("Dog")
  }

  trait ColdBlooded

  trait Carnivore {
    def eat(animal: Animal): Unit

    val prefferedMeal: String = "fresh meat"
  }

  class Crocodrile extends Animal with Carnivore with ColdBlooded {
    override val createType: String = "CROC"

    override def eat = println("Nom nom")

    def eat(animal: Animal): Unit = println(s"I am a croc and I am eating : ${animal.createType}")
  }

  val dog = new Dog
  val croc = new Crocodrile
  croc.eat(dog)


}
