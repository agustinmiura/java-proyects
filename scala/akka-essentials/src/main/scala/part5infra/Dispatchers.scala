package part5infra


import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

import scala.concurrent.ExecutionContext
import scala.util.Random

object Dispatchers extends App {

  class Counter extends Actor with ActorLogging {
    var count = 0

    override def receive: Receive = {
      case message =>
        count += 1
        log.info(s"count $message")
    }

  }

  val system = ActorSystem("DispatchersDemo")//ConfigFactory.load().getConfig("dispatchersDemo")

  //method #1
  val actors = for (i <- 1 to 10) yield system.actorOf(Props[Counter].withDispatcher("my-dispatcher"), s"counter_$i")
  val random = new Random()
  /*
  for (i <- 1 to 1000) {
    actors(random.nextInt(10)) ! i
  }
  */

  //method #2 from config
  val rtjvmActor = system.actorOf(Props[Counter],"rtjvm")

  import system.dispatcher

  class DbActor extends Actor with ActorLogging {
    implicit val executionContext: ExecutionContext = context.system.dispatchers.lookup("my-dispatcher")
    override def receive: Receive = {
      case message =>
        Thread.sleep(5000)
        log.info(s"Sucess: $message")
    }
  }

  val dbActor = system.actorOf(Props[DbActor])
  //dbActor ! "The meaning of life is 42!"

  val nonBlockingACtor = system.actorOf(Props[Counter])
  for (i <- 1 to 1000 ) {
    val message = s"Important mesage $i"
    dbActor ! message
    nonBlockingACtor ! message
  }


}
