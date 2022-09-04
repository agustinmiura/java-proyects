
object MethodNotations extends App {

  class Person(val name: String, favouriteMovie: String, val age: Int = 0) {
    def likes(movie: String) = favouriteMovie.equals(movie)

    def unary_! : String = s"$name, What the heck"

    def isAlive = true

    def apply() = s"Hi, my name is $name and I like $favouriteMovie"

    def +(value: String) = new Person(s"$name $value ", favouriteMovie, age)

    def unary_+ : Person = new Person(s"$name the rockstar ", favouriteMovie, age + 1)

    def learns(sentence: String) = s"${name} learns ${sentence}"

    def learns(): String = this learns "Scala"

    def apply(times: Number): String = s"Mary watched ${times} ${favouriteMovie}"
  }

  val mary = new Person("Mary", "ET", 99)
  println(mary.likes("ET"))
  println(mary likes "ET")

  val tom = new Person("Tom", "Movie")
  println(1.+(2))

  val x = -1
  val y = 1.unary_-

  println(!mary)
  println(mary.apply())
  println(mary())

  println((mary + "aString").name)
  println((+mary).age)

  println(mary learns "Java")
  println(mary.learns())

  println(mary(10))
}
