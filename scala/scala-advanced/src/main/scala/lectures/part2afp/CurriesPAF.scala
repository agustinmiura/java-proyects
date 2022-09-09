package lectures.part2afp

object CurriesPAF extends App {

  val superAdder: Int => Int => Int =
    x => y => y + y

  val add3 = superAdder(3)
  println(add3(5))
  println(superAdder(3)(5))

  def curriedAdder(x: Int)(y: Int): Int = x + y

  val add4: Int => Int = curriedAdder(4)

  def inc(x:Int): Int = x + 1

  List(1,2,3).map(inc)

  val add5 = curriedAdder(5)
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x:Int)(y:Int) = x + y

  val add7 = (x:Int) => simpleAddFunction(7,x)
  val add7_2 = simpleAddFunction.curried(7)
  val add7_3 = curriedAddMethod(7)
  val add7_4 = curriedAddMethod(7)(_)
  val add7_5 = simpleAddMethod(7, _: Int)
  val add7_6 = simpleAddMethod(7, _: Int)

  def concatenator(a:String, b:String, c:String): String = a + b + c
  val insertName = concatenator("Hello, I am ", _: String, ", how are you?")
  println(insertName("Daniel"))

  val fillInTheBlanks = concatenator("Hello, ", _: String, _: String)
  println(fillInTheBlanks("Daniel", "Scala is awesome"))

  "%4.2f".format(Math.PI)
  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(curriedFormatter("%8.6f")))

  byName(23)
  byName((method))
  byName(parenMethod())
}
