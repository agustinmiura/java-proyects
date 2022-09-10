package lectures.part4implicits

import exercises.EqualityPlayground.{Equal, anotherJohn}
import lectures.part4implicits.TypeClasses.HTMLSerializer.serializeToHtml

import java.util.Date

object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name, $age, $email</div>"
  }

  User("John", 32, "john@ghmail.com").toHtml

  object HTMLSerializer {
    def serializeToHtml(value: Any) = value match {
      case User(n, e, b) => "User"
      case _ => "html"
    }
  }

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name}, ${user.age}, ${user.email}<div>"
  }
  val john = User("John", 99, "email@gmail.com")
  println(UserSerializer.serialize(john))

  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  println(HTMLSerializer.serializeToHtml(42))
  println(HTMLSerializer.serializeToHtml(john))

  implicit class HtmlEnrichment[T](value: T) {
    def toHtml(serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  println(2.toHtml)

  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, other)
    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = ! equalizer.apply(value, other)
  }

  println(john === anotherJohn)

  
}
