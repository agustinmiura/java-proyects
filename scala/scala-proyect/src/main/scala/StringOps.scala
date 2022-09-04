
object StringOps extends App {

  var str: String = "Hello I am learning Scala"

  println(str.charAt(10))
  println(str.substring(2))
  println(str.split(" ").toList)
  println(str.startsWith("He"))
  println(str.length)

  val aNumberString = "45"
  val aNumber = aNumberString.toInt
  println('a' +: aNumberString :+ 'z')
  println("Content".reverse)
  println("Content".take(2))

  val name = "David"
  val age = 12
  val greeting = s"Hello, my name is $name , and my age is $age"
  println(s" Greeting is $greeting")
  val anotherGreeting = s"Hello my name is ${age + 1}"
  println(s"Greeting : $anotherGreeting")

  val speed = 1.2f
  val myth = f"$name can eat $speed%2.2f"
  println(s"myth is $myth")

  println(raw"This is a \n newline")
  val escaped = "This is a \n newline"
  println(raw"$escaped")

}
