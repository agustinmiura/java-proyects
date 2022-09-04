package com.rockthejvm.part1basics


object Functions extends App {

  def aFunction(a: String, b: Int): String = {
    a + " " + b
  }

  println(aFunction("Hola", 10))

  def aParameterlessFn(): Int = 42

  println(aParameterlessFn())

  def anotherFn(aString: String, n: Int): String = {
    if (n == 1) {
      aString
    } else {
      aString + anotherFn(aString, n - 1)
    }
  }

  def aFunctionWithSideEffects(aString: String): Unit = println(aString)

  def aBigFunction(n: Int): Int = {
    def aSmallerFunction(a: Int, b: Int): Int = a + b

    aSmallerFunction(n, n - 1)
  }

  def greetingFn(name: String, age: Int) = println(" Hi my name is : " + name + " and I am " + age)

  def factorialFn(n: Int): Int = {
    if (n <= 1) {
      1
    }
    else {
      n * factorialFn(n - 1)
    }
  }

  def fibonacci(n: Int): Int = {
    if (n == 1 || n == 2) {
      1
    }
    else {
      fibonacci(n - 1) + fibonacci(n - 2)
    }
  }

  def isPrime(n: Int): Boolean = {
    def isPrimeUntil(t: Int): Boolean = {
      if (t <= 1) true
      else n % t != 0 && isPrimeUntil(t - 1)
    }

    isPrimeUntil(n / 2)
  }

  def nextInt(x: Int) = x + 1

  println(nextInt(37))


}
