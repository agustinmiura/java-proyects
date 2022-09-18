package part2actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.event.Logging

object ActorLoggingDemo extends App {

  class SimpleActor extends Actor {
    val logger  = Logging(context.system, this)
    override def receive: Receive = {
      case message => logger.info(message.toString)
    }
  }

  val system = ActorSystem("LoggingDemo")
  val actor = system.actorOf(Props[SimpleActor], "simpleActor")

  actor ! "Logging a simple message"

  class ActorWithLogging  extends Actor with ActorLogging {
    override def receive: Receive = {
      case  (a, b) => log.info("Two things: {} and {}", a, b)
      case message => log.info(message.toString)
    }
  }

  val simpleActor = system.actorOf(Props[ActorWithLogging], "ActorWithLogging")
  simpleActor ! "Logging with a simple actor"

}
