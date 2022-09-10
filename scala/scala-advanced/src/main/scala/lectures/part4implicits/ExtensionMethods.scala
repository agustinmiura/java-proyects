package lectures.part4implicits

object ExtensionMethods extends App {

  case class Person(name: String) {
    def greet(): String = s" Hi I am $name "
  }

  extension (string: String) {
    def greetAsPerson(): String = Person(string).greet()
  }

  val danielsGreeting = "Daniel".greetAsPerson()

  extension (value: Int) {
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
  }

  val is3Even = 3.isEven

  extension [A](list: List[A]) {
    def ends: (A,A) = (list.head, list.last)
    def extremes(using ordering: Ordering[A]): (A, A) = list.sorted.ends
  }



}
