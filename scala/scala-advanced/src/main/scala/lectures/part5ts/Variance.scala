package lectures.part5ts

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodrile extends Animal

  class Cate[T]
  class CCage [+T]

  val cCage: CCage[Animal] = new CCage[Cat]

  class ICage[T]

  class XCage[-T]
  val xCage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T)

  class CovariantCage[+T](val animal: T)

  class AnotherContraVariantCage[-T] {
    def addAnimal(animal: T) = true
  }

  val acc: AnotherContraVariantCage[Cat] = new AnotherContraVariantCage[Animal]
  acc.addAnimal(new Cat())
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B]
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)

  class Petshop[-T] {
    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: Petshop[Dog] = new Petshop[Animal]

  class Terranove extends Dog

  val bigFurry = shop.get(true, new Terranove)

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  class IList[T]

  class IParking[T](vehicle: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???
    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): IParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???
    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound[S <: T](vehicles: List[S]): IParking[S] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???
    def flatMap[R <: T,S](f: R => XParking[S]): XParking[S] = ???
  }

  class CParking2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[S] = ???
    def impound[S >: T](vehicles: IList[S]): CParking[S] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  class XParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound[S <: T](vehicles: IList[S]): IParking[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }

}
