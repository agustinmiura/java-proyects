package part2EvenSourcing

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, Recovery, RecoveryCompleted, SnapshotSelectionCriteria}

object RecoveryDemo extends App {

  case class Command(content: String)

  case class Event(id:Int, content: String)

  class RecoveryActor extends PersistentActor with ActorLogging {

    var latestPersistedId = 0

    override def persistenceId: String = "recovery-actor"

    override def receiveCommand: Receive = online(0)

    def online(latestPersistedId:Int): Receive = {
      case Command(content) =>
        persist(Event(latestPersistedId, content)) { event =>
          log.info(s"Successfully persisted $event, recovery : ${if (this.recoveryFinished) "" else "NOT"} finished")
          context.become(online(latestPersistedId +1))
        }
    }

    override def receiveRecover: Receive = {
      case Event(id,message) =>
        log.info(s"Recovered $message")
        context.become(online(id))
      case RecoveryCompleted =>
        log.info("I have finished recovering")
    }
    override def onRecoveryFailure(cause:Throwable, event: Option[Any]): Unit = {
      log.error(s"Errpr")
      super.onRecoveryFailure(cause, event)
    }

    override def recovery: Recovery = Recovery(fromSnapshot = SnapshotSelectionCriteria.None)

  }

  val system = ActorSystem("RecoveryDemo")
  val recoveryActor = system.actorOf(Props[RecoveryActor], "recoveryActor")

  for(i <- 1 to 1000) {
    recoveryActor ! Command(s"Command $i")
  }


}
