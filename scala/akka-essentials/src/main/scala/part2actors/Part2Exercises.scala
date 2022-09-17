package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object Part2Exercises extends App {

  /**
   * Counting with actors
   */
  class ActorCounter extends Actor {
    var counter = 0

    override def receive: Receive = {
      case Increment(amount) =>
        //println(s"[ActorCounter] Increment : ${amount}")
        counter += amount
      case Decrement(amount) =>
        //println(s"[ActorCounter] Decrement : ${amount}")
        counter -= amount
      case print: Print => println(s"[ActorCounter] counter : ${counter}")
      case _ => println("Message not matched")
    }
  }

  case class Increment(delta: Int)

  case class Decrement(delta: Int)

  case class Print()

  val counterSystem = ActorSystem("counterSystem")
  val counterActor = counterSystem.actorOf(Props[ActorCounter], "actorCounter")
  /*
  counterActor ! Increment(1)
  counterActor ! Print()
  counterActor ! Decrement(1)
  counterActor ! Print()
  */
  /**
   * Bank account actor
   */

  case class Deposit(amount: Double)

  case class Withdraw(amount: Double)

  case class SuccessResult()

  case class FailureResult(message: String)

  case class Statement()

  class BankActor extends Actor {
    var money: Double = 0.0

    override def receive: Receive = {
      case Deposit(amount) =>
        println("[BankActor] deposit")
        money += amount
      case Withdraw(amount) =>
        println("[BankActor] withdraw")
        if (amount <= money) {
          money -= amount
          SuccessResult
        } else {
          FailureResult
        }
      case Statement => println(s"[BankActor] the statement is $money")
      case DepositAction(amount, ref) =>
        println("[BankActor] DepositAction")
        money += amount
        ref ! SuccessResult
      case WithdrawAction(amount, ref) =>
        println("[BankActor] WithdrawAction")
        if (amount <= money) {
          money -= amount
          ref ! SuccessResult
        } else {
          ref ! FailureResult("Not enough money")
        }
      case _ => println(s"No matching ")
    }
  }

  case class DepositAction(amount: Double, ref: ActorRef)

  case class WithdrawAction(amount: Double, ref: ActorRef)

  class TestActor extends Actor {
    override def receive: Receive = {
      case DepositAction(amount, actor) =>
        println("[TestActor]Deposit")
        actor ! (DepositAction(amount, self))
      case WithdrawAction(amount, actor) =>
        println("[TestActor]Withdraw")
        actor ! (WithdrawAction(amount, self))
      case SuccessResult => println("Operation success")
      case FailureResult(message) => println(s"Operation failure $message")
    }
  }

  val bankSystem = ActorSystem("bankSystem")
  val bankActor = bankSystem.actorOf(Props[BankActor], "bankActor")
  val testActor = bankSystem.actorOf(Props[TestActor], "testActor")

  /*
  bankActor ! Deposit(10.0)
  bankActor ! Statement
  bankActor ! Withdraw(0.1)
  bankActor ! Statement
  */

  testActor ! DepositAction(10.0, bankActor)
  //testActor ! WithdrawAction(100.0, bankActor)
  testActor ! WithdrawAction(0.1, bankActor)

  println("Finished message")
}
