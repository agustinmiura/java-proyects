package lectures.part4implicits

object PimpMyLibrary extends App {

  implicit class RichInt(value: Int) {
    def isEven: Boolean = value % 2 == 0

    def squareRoot: Double = Math.sqrt(value)

    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if (n <= 0) List()
        else concatenate(n - 1) ++ list

      concatenate(value)
    }
  }

  /*
  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }
  */

  42.squareRoot
  42.isEven

  implicit class RichString(string: String) {
    def asInt: Int = Integer.valueOf(string)

    def encrypt(cypherDistance: Int): String = string.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  println("3".asInt + 4)
  println("John".encrypt(2))

  implicit def stringToInt(string: String): Int = Integer.valueOf(string)

  println("6" / 2)

  class RichAltInt(value: Int)

  implicit def intToBoolean(i: Int): Boolean = i == 1

  def aConditionValue = if (3) "OK" else "Something wrong"

  println(aConditionValue)

}
