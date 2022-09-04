package com.rockthejvm.part3fp

object TuplesAndMaps extends App {

  val aTuple:Tuple2[Int,String] = new Tuple2(2,"Hello Scala")
  println(aTuple)

  val anotherTuple = (2, "HelloScala")
  val anotherTuple3 = (1,"Hello", 'a')

  println(anotherTuple._1)
  println(anotherTuple._2)
  println(anotherTuple.copy(_2 = " Goodbye language"))
  println(anotherTuple.swap)

  val map: Map[String, Int] = Map()
  println(map)

  val phoneBook = Map(("Jim",555),("John",222),("John2",22))
  val anotherPhoneBook = Map("Jim" -> 111, "John" -> 222).withDefaultValue(-1)

  println(anotherPhoneBook)

  println(anotherPhoneBook.contains("Jim"))
  println(anotherPhoneBook("Jim"))
  println(anotherPhoneBook("None"))

  val newPairing = "Mary" -> 678
  val newPhoneBook = anotherPhoneBook + newPairing
  println(newPhoneBook)

  println(anotherPhoneBook.map(pair => pair._1.toLowerCase -> pair._2))
  println(anotherPhoneBook.withFilter(x => x._1.startsWith("J")))
  println(anotherPhoneBook.view.mapValues(number => number * 10).toMap)

  println(anotherPhoneBook.toList)
  println(List(("Daniel", 555)).toMap)
  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  println(names.groupBy(name => name.charAt(0)))

}
