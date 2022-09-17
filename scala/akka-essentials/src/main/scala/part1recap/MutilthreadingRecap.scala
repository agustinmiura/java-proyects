package part1recap

import scala.concurrent.Future
import scala.util.{Failure, Success}

object MutilthreadingRecap extends App {

  val aThread = new Thread(() =>{
    println("I am running in parallel")
  })

  aThread.start();

  val threadHello = new Thread(() => (1 to 1000).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 1000).foreach(_ => println("goodbye")))

  threadHello.start()
  threadGoodbye.start()

  class BankAccount(@volatile private var amount: Int) {
    override def toString: String = "" + amount
    def withdraw(money: Int) = this.amount -= money
    def safeWithdraw(money: Int) = this.synchronized {
      this.amount -= money
    }
  }

  import scala.concurrent.ExecutionContext.Implicits.global
  val future = Future {
    42
  }
  future.onComplete {
    case Success(42) => println(" Solution here ")
    case Failure(_) => println(" An error ")
  }

  val aProcessFuture = future.map(_ + 1)
  val aFlatFuture = future.flatMap { value =>
    Future(value + 2)
  }
  val filteredFuture = aFlatFuture.filter(_ % 2 ==0)
  val aNonSense = for {
    aVar <- future
    filtered <- filteredFuture
  } yield aVar + filtered




}
