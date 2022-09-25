package part3

import akka.actor.ActorLogging
import akka.persistence.PersistentActor
import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted, SaveSnapshotFailure, SaveSnapshotSuccess, SnapshotOffer}
import com.typesafe.config.ConfigFactory

class SimplePersistanceActor extends PersistentActor with ActorLogging {
  override def persistenceId: String = "simple-persistent-actor"

  var nMessages = 0

  override def receiveCommand: Receive = {
    case "print" =>
      log.info(s"I have persisted $nMessages")
    case "snap" =>
      saveSnapshot(nMessages)
    case SaveSnapshotSuccess =>
      log.info("success saving")
    case SaveSnapshotFailure(metadata, cause) =>
      log.info(s"failed saving snapshot $metadata, $cause")
    case message =>
      persist(message) { _ =>
        log.info(s"Persisting $message")
        nMessages += 1
      }
  }

  override def receiveRecover: Receive = {
    case RecoveryCompleted =>
      log.info("Recovery done")
    case SnapshotOffer(metadata, payload: Int) =>
      log.info(s"Recovered snapshot $payload")
      nMessages += 1
    case message =>
      log.info(s"message : ${message.toString}")
      nMessages += 1
  }
}
