package part2EvenSourcing

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.persistence.PersistentActor

object PersistAsyncDemo extends App {

  case class Command(content: String)

  case class Event(content: String)

  object CriticalProcessor {
    def props(eventAggregator: ActorRef) = Props(new CriticalProcessor(eventAggregator))
  }

  class CriticalProcessor(eventAggregator: ActorRef) extends PersistentActor with ActorLogging {
    override def persistenceId: String = "stream-processor"

    override def receiveCommand: Receive = {
      case Command(content) =>
        eventAggregator ! s"Processing $content"
        persistAsync(Event(content)) { event =>
          eventAggregator ! event
        }

        val processedContents = content +  "_processed"
        persistAsync(Event(processedContents)) { event =>
          eventAggregator ! event
        }
    }

    override def receiveRecover: Receive = {
      case message => log.info(s"Recovered $message")
    }
  }

  class EventAggregator extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(s"Aggregating $message")

    }
  }

  val system = ActorSystem("persistedAsyncDemo")
  val eventAggregator = system.actorOf(Props[EventAggregator], "eventAggregator")
  val streamProcessor = system.actorOf(CriticalProcessor.props(eventAggregator), "streamProcessor")

  streamProcessor ! Command("command1")
  streamProcessor ! Command("command2")

}
