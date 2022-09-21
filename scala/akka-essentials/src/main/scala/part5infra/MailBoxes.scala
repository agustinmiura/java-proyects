package part5infra

import akka.actor.{Actor, ActorLogging, ActorSystem, PoisonPill, Props}
import akka.dispatch.{ControlMessage, PriorityGenerator, UnboundedPriorityMailbox}
import com.typesafe.config.{Config, ConfigFactory}

object MailBoxes extends App {

  val system = ActorSystem("MailboxDemo", ConfigFactory.load().getConfig("mailboxesDemo"))

  class SimpleActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }

  case object ManagementTicket extends ControlMessage
  val aSystem = system.actorOf(Props[SimpleActor].withMailbox("control-mailbox"))
  /*
  aSystem ! "[P0] this needs to be solved NOW!"
  aSystem ! "[P1] do this when you have the time"
  aSystem ! ManagementTicket
  */
  
  val altControlAwareActor = system.actorOf(Props[SimpleActor], "altControlAwareActor")
  altControlAwareActor ! "[P0] this needs to be solved NOW!"
  altControlAwareActor ! "[P1] do this when you have the time"
  altControlAwareActor ! ManagementTicket

}
