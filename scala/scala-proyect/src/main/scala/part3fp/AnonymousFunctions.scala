package ar.com.name
package part3fp

object AnonymousFunctions extends App {

  val doubler: Int => Int = x => x * 2

  val adder: (Int,Int) => Int = (a:Int,b:Int) => a + b

  var justDoSomething = () => 3

  println(justDoSomething())

  val stringToInt = { (str:String) =>
    str.toInt
  }

  val niceIncrementor: Int => Int = (x:Int) => x + 1
  val niceIncrementor2: Int => Int = _ + 1
  val niceAdder2: (Int,Int) => Int = _ + _

  /*
  * val superAdder: Function[Int, Function[Int,Int]] = new Function[Int, Function1[Int,Int]] {
    override def apply(x: Int): Function[Int, Int] = new Function[Int, Int] {
      override def apply(y: Int): Int = x + y
    }
  }
  **/



}
