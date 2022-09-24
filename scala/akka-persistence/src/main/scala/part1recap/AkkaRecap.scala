package part1recap

import akka.actor.SupervisorStrategy.{Restart, Stop}
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props, Stash, SupervisorStrategy}

object AkkaRecap extends App {

  class SimpleActor extends Actor with ActorLogging with Stash {
    override def receive: Receive = {
      case "createChild" =>
        val child = context.actorOf(Props[SimpleActor], "myChild")
        child ! "say hello"
      case "stash" =>
        stash()
      case "change handler NOW" =>
        unstashAll()
        context.become(anotherHandler)
      case message => println(s" I receive the message $message")
    }

    def anotherHandler: Receive = {
      case message => println(message.toString)
    }

    override def preStart(): Unit = {
      log.info("I am starting")
    }

    override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
      case _: RuntimeException => Restart
      case _ => Stop
    }
  }

}
