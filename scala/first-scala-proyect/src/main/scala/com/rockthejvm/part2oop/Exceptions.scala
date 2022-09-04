package com.rockthejvm.part2oop

object Exceptions extends App {

  val x: String = null

  class MyClass {
    def getInt(withExceptions: Boolean): Int =
      if (withExceptions) throw new RuntimeException(" No int for you ")
      else 42
  }

  val potentialFail = try {
    val myClass = new MyClass
    myClass.getInt(true)
  } catch {
    case e: RuntimeException => 43
  } finally {
    println("Finally")
  }

  println(potentialFail)

  class OverflowException extends RuntimeException

  class UnderflowException extends RuntimeException

  class MyException extends Exception

  val exception = new MyException

  object PocketCalculator {
    def add(x: Int, y: Int): Int = {
      val result = x + y
      if (x > 0 && y > 0 && result < 0)
        throw new OverflowException
      else if (x < 0 && y < 0 && result > 0)
        throw new UnderflowException
      else result
    }

    def substract(x: Int, y: Int): Int = {
      val result = x - y
      if (x > 0 && y < 0 && result < 0) throw new OverflowException
      else if (x < 0 && y > 0 && result > 0) throw new UnderflowException
      else result
    }

    def multiply(x: Int, y: Int): Int = {
      val result = x * y
      if (x > 0 && y > 0 && result < 0) throw new UnderflowException
      else if (x > 0 && y < 0 && result > 0) throw new UnderflowException
      else if (x < 0 && y > 0 && result > 0) throw new UnderflowException
      else result
    }

    def divide(x: Int, y: Int): Int = x / y


  }

  println(PocketCalculator.add(1, 2))

}
