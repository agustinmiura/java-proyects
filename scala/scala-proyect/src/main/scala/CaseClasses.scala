
object CaseClasses extends App {

  case class Person(name: String, age: Int)

  val jim = new Person("Jim", 99)
  val clonedJim = new Person("Jim", 99)

  println(jim.name)

  println(jim)

  println(jim == clonedJim)

  val cloned = jim.copy(age = 900)

  println(cloned.toString)

  val thePerson = Person

  val mary = Person("mary", 23)

  case object UnitedKingdom {
    def name: String = "The UK of GB and NI"
  }
}
