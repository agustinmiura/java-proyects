package part4

import java.io.File

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorLogging, ActorSystem, OneForOneStrategy, Props}
import akka.pattern.{Backoff, BackoffSupervisor}

import scala.concurrent.duration._
import scala.io.Source

object BackoffSupervisorPattern extends App {

  case object Readfile

  class FileBasedPersistentActor extends Actor with ActorLogging {
    var dataSource: Source = null

    override def preStart(): Unit = {
      log.info("Persistent actor starting")
    }

    override def postStop(): Unit = {
      log.error("Persistent actor has stopped")
    }

    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
      log.error("Persistant actor restarting")
    }

    override def receive: Receive = {
      case Readfile =>
        if (dataSource == null)
          dataSource = Source.fromFile(new File("src/main/resources/testfiles/important_data.txt"))
        log.info(" I have just read data FROM " + dataSource.getLines().toList)
    }

  }

  val system = ActorSystem("backoffSupervisorDemo")

  /*
  val simpleActor = system.actorOf(Props[FileBasedPersistentActor])
  simpleActor ! Readfile
  */

  val simpleSupervisorProps = BackoffSupervisor.props(
    Backoff.onFailure(
      Props[FileBasedPersistentActor],
      "simpleBackoffActor",
      3 seconds, // then 6s, 12s, 24s
      30 seconds,
      0.2
    )
  )

  val simpleBackoffSupervisor = system.actorOf(simpleSupervisorProps, "simpleSupervisor")
  simpleBackoffSupervisor ! Readfile

  val stopSupervisorProps = BackoffSupervisor.props(
    Backoff.onStop(
      Props[FileBasedPersistentActor],
      "stopBackoffActor",
      3 seconds,
      30 seconds,
      0.2
    ).withSupervisorStrategy(
      OneForOneStrategy() {
        case _ => Stop
      }
    )
  )

  //val stopSupervisor = system.actorOf(stopSupervisorProps, "stopSupervisor")
  //stopSupervisor ! Readfile

  class EagerFBPersistantActor extends FileBasedPersistentActor {
    override def preStart(): Unit = {
      log.info("Eager actor starting")
      dataSource = Source.fromFile(new File("src/main/resources/testfiles/important_data.txt"))
    }
  }

  val repeatedSupervisor = BackoffSupervisor.props(
    Backoff.onStop(
      Props[EagerFBPersistantActor],
      "eagerActor",
      1 second,
      30 seconds,
      0.1
    )
  )

  val eager = system.actorOf(repeatedSupervisor, "eagerSupervisor")

}
