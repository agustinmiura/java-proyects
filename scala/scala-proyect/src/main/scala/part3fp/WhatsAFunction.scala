package ar.com.name
package part3fp

object WhatsAFunction extends App {

  val doubler = new MyFunction[Int,Int] {
    override def apply(element:Int): Int = element * 2
  }
  println(doubler(2))

  val stringToIntConverter = new Function1[String, Int] {
    override def apply(string:String): Int = string.toInt
  }
  println(stringToIntConverter("3") + 4)

  val adder:((Int,Int) => Int)= new Function2[Int,Int,Int] {
    override def apply(a:Int, b:Int): Int = a + b
  }

  val stringJoiner:((String,String) => String) = new Function2[String,String,String] {
    override def apply(a:String, b:String):String = a + b
  }

  val superAdder: Function[Int, Function[Int,Int]] = new Function[Int, Function1[Int,Int]] {
    override def apply(x: Int): Function[Int, Int] = new Function[Int, Int] {
      override def apply(y: Int): Int = x + y
    }
  }

  val superAdder2 =  (x:Int) => (y:Int) => x + y

  val adder3 = superAdder(3)
  println(adder3(4))
  println(superAdder(3)(4))
  println(superAdder2 (3) (4))

}

trait MyFunction[A, B] {
  def apply(element: A): B
}