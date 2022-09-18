package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.Part2Exercises.Person.Client

object Part2Exercises extends App {

  object ActorCounter {
    case object Increment

    case object Decrement

    case object Print
  }

  /**
   * Counting with actors
   */
  class ActorCounter extends Actor {

    import ActorCounter._

    var counter = 0

    override def receive: Receive = {
      case Increment =>
        counter += 1
      case Decrement =>
        counter -= 1
      case Print => println(s"[ActorCounter] counter : ${counter}")
      case _ => println("Message not matched")
    }
  }

  val counterSystem = ActorSystem("counterSystem")
  val counterActor = counterSystem.actorOf(Props[ActorCounter], "actorCounter")

  /*
  counterActor ! ActorCounter.Increment
  counterActor ! ActorCounter.Print
  counterActor ! ActorCounter.Decrement
  counterActor ! ActorCounter.Print
  */

  /**
   * Bank account actor
   */
  object BankActor {
    case class Deposit(amount: Double)

    case class Withdraw(amount: Double)

    case class SuccessResult(message: String)

    case class FailureResult(message: String)

    case object Statement
  }

  class BankActor extends Actor {
    var money: Double = 0.0

    import BankActor._

    override def receive: Receive = {
      case Deposit(amount) =>
        println("[BankActor] deposit")
        money += amount
        sender() ! SuccessResult(s"Success deposit $amount")
      case Withdraw(amount) =>
        println("[BankActor] withdraw")
        if (amount <= money) {
          money -= amount
          sender() ! SuccessResult(s"Withdraw success $amount")
        } else {
          sender() ! FailureResult("Invalid amount")
        }
      case Statement => sender() ! s"Your balance is funds $money"
      case _ => println(s"No matching ")
    }
  }

  object Person {
    case class Client(account: ActorRef)
  }

  class Person extends Actor {

    import BankActor._
    import Person._

    override def receive: Receive = {
      case Client(account) =>
        account ! Deposit(10)
        account ! Withdraw(1.0)
        account ! Withdraw(2.0)
        account ! Statement
      case SuccessResult(message) => println(s"Success result $message")
      case FailureResult(message) => println(s"Failure : $message")
      case message: String => println(message)
      case _ => println("Not matching")
    }
  }

  val bankSystem = ActorSystem("bankSystem")
  val bankActor = bankSystem.actorOf(Props[BankActor], "bankActor")
  val testActor = bankSystem.actorOf(Props[Person], "personActor")

  testActor ! Client(bankActor)

  println("Finished message")
}
