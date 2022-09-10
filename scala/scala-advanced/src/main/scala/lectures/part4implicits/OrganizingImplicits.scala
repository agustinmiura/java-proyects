package lectures.part4implicits

object OrganizingImplicits extends App {

  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  println(List(1, 2, 244, 12).sorted)

  case class Person(name: String, age: Int)

  implicit val orderingPersons: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )
  println(persons.sorted)

  case class Purchase(unitQty: Int, unitPrice: Double)
  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a,b) => a.unitQty*b.unitPrice<b.unitQty*b.unitPrice)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitQty <  _.unitQty)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice <  _.unitPrice)
  }


}
