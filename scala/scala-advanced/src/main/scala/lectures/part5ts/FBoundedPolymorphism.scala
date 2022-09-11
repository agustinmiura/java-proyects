package lectures.part5ts

object FBoundedPolymorphism {
  /*
  trait Animal {
    def breed: List[Animal]
  }
  class Cat extends Animal {
    override def breed: List[Cat] = ???
  }
  class Dog extends Animal {
    override def breed: List[Dog] = ???
  }
  */
  trait Animal[A <: Animal[A]] { self: A =>
    def breed: List[Animal[A]]
  }

  class Cat extends Animal[Cat] {
    override def breed: List[Animal[Cat]] = ???
  }

  class Dog extends Animal[Dog] {
    override def breed: List[Animal[Dog]] = ???
  }

  trait Entity[E <: Entity[E]] //ORM

  class Person extends Comparable[Person] {
    override def compareTo(o: Person): Int = ???
  }

  class Crocodrile extends Animal[Crocodrile] {
    override def breed: List[Animal[Crocodrile]] = ???
  }

  
}
