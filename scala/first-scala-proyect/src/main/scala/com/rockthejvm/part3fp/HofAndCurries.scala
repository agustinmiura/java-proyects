package com.rockthejvm.part3fp

object HofAndCurries extends App {

  val superFunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = null

  def nTimes(f: Int => Int, n:Int, x:Int): Int =
    if (n <= 0) x
    else nTimes(f,n-1, f(x))

  val plusOne = (x:Int) => x + 1
  println(nTimes(plusOne, 10, 1))

  def nTimesBetter(f:Int => Int, n:Int): (Int => Int) =
    if (n<=0) (x:Int) => x
    else (x:Int) => nTimesBetter(f, n -1)(f(x))

  val plus10 = nTimesBetter(plusOne, 10)
  println(plus10(1))

  val superAdder3: Int => (Int => Int) = (x:Int) => (y:Int) => x + y
  val add3 = superAdder3(3)
  println(add3(10))
  println(superAdder3(3)(10))

  def curriedFormatter(c: String)(x:Double): String = c.format(x)

  val standardFormat: (Double => String) = curriedFormatter("%4.2f")

  val preciseFormat: (Double => String) = curriedFormatter("%10.8f")

  
  
  println(standardFormat(Math.PI))
  println(preciseFormat(Math.PI))

}
