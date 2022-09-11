package lectures.part5ts

object FboundExercise extends App {

  /*
  trait Animal
  trait CanBreed[A] {
    def breed(a: A): List[A]
  }
  class Dog extends Animal
  object Dog {
    implicit object DogsCanBreed extends CanBreed[Dog] {
      def breed(a: Dog): List[Dog] = List()
    }
  }
  implicit class CanBreedOps[A](animal: A) {
    def breed(implicit canBreed: CanBreed[A]): List[A] =
      canBreed.breed(animal)
  }

  class Cat extends Animal
  object Cat {
    implicit object CatsCanBreed extends CanBreed[Dog] {
      def breed(a: Dog): List[Dog] = List()
    }
  }

  val dog = new Dog
  dog.breed
  */

  trait Animal[A] {
    def breed(a: A): List[A]
  }

  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }
  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog()
  dog.breed

    
}
