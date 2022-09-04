package com.rockthejvm.part2oop

object Objects extends App {
  object Person {
    val N_EYES = 2

    def canFly = false

    def apply(mother: Person, father: Person) = new Person("Bobbie")
  }

  class Person(val name: String = "") {

  }

  println(Person.N_EYES)
  println(Person.canFly)

  val mary = new Person
  val john = new Person
  println(s" I see : ${mary == john}")

  val person1 = Person
  val person2 = Person
  println(s" I see : ${person1 == person2}")
  val bobbie = Person.apply(mary, john)
  val boobie2 = Person(mary, john)

}
