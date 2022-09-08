package lectures.part2afp

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1

  val aFussyFunction = (x:Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x ==5) 999
    else throw new FunctionNotApplicationException

  class FunctionNotApplicationException extends RuntimeException

  val aFussyFunctionNicer = (x:Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  val aPartialFunction: PartialFunction[Int,Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  println(aPartialFunction(2))
  println(aPartialFunction(5))

  println(aPartialFunction.isDefinedAt(76))

  val lifted = aPartialFunction.lift
  println(lifted(2))
  println(lifted(-2))

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  val aMappedList = List(1,2,3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 100
  }
  println(aMappedList)

  val partialChatBot: PartialFunction[String, String] = {
    case "Hello" => "How are you?"
    case "Whats your name" => "My name is "
    case "Thanks" => "Np"
    case _ => "Another message"
  }

  scala.io.Source.stdin.getLines().map(partialChatBot).foreach(println)

}
