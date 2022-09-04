package com.rockthejvm.part2oop


object OOBasics extends App {
  val person = new Person("name", 10)
  println(person.x)
  person.greet("to")
  person.greet()

  val author = new Writer("Charles", "Dickers", 1812)
  val novel = new Novel("Great Expectations", 1861, author)

  val counter = Counter(0)
  counter.inc.print
  counter.inc.inc.inc.print

  counter.inc(20).print
}

class Person(val name: String, val age: Int = 0) {
  val x = 2
  println(1 + 3)

  def greet(name: String): Unit = println(s"${this.name} says: Hi , $name")

  def greet() = println(s"Hi I am $name")

  def this(name: String) = this(name, 0)

  def this() = this("John Doe")
}

/**
 * ar.com.name.other.Novel
 * ar.com.name.other.Writer
 *
 */
class Writer(firstName: String, surname: String, val year: Int) {
  def fullName(): String = {
    return s"$firstName, $surname"
  }
}

class Novel(name: String, yearOfRelease: Int, val author: Writer) {
  def authorAge(authorYear: Int) = yearOfRelease - author.year

  def copy() = new Novel(name, yearOfRelease, author);
}

class Counter(val value: Int) {

  def inc = {
    println(" Incrementing ")
    new Counter(value + 1)
  }

  def dec = {
    println(" Decrementing ")
    new Counter(value - 1)
  }

  def inc(n: Int): Counter = {
    if (n <= 0) this
    else inc.inc(n - 1)
  }

  def dec(n: Int): Counter = {
    if (n <= 0) this
    else dec.dec(n - 1)
  }

  def print = println(value)

}