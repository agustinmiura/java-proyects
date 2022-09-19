package part4

import akka.actor.{Actor, ActorLogging, Props}
import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{CallingThreadDispatcher, TestActorRef, TestProbe}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import scala.concurrent.duration._

object ActorLifeCycle extends App {

  object StartChild {
    def apply: Any = ???
  }

  class LifeCycleActor extends Actor with ActorLogging {

    override def preStart(): Unit = {
      log.info("I am starting")
    }

    override def postStop(): Unit = {
      log.info("I have stopped")
    }

    override def receive: Receive = {
      case StartChild =>
        context.actorOf(Props[LifeCycleActor], "child")
    }
  }

  val system = ActorSystem("LifeCycleDemo")
  /*
  val system = ActorSystem("LifeCycleDemo")
  val parent = system.actorOf(Props[LifeCycleActor], "parent")
  parent ! StartChild
  parent ! PoisonPill
  */

  object Fail
  object FailChild
  object CheckChild
  object Check

  class Parent extends Actor with ActorLogging {
    val child = context.actorOf(Props[Child], "supervisedChild")

    override def receive: Receive = {
      case FailChild => child ! Fail
      case CheckChild => child ! Check
    }
  }

  class Child extends Actor with ActorLogging {
    override def preStart(): Unit = {
      log.info("Child started")
    }

    override def postStop(): Unit = {
      log.info("Child post stop")
    }

    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
      log.info(s" Child restarting because of ${reason.getMessage}")
    }

    override def postRestart(reason: Throwable): Unit = {
      log.info(s" Child has restarted ")
    }

    override def receive: Receive = {
      case Fail =>
        log.warning("Child will fail")
        throw new RuntimeException("I have failed")
      case Check =>
        log.info("Alive and kicking")
    }
  }

  val supervisor = system.actorOf(Props[Parent], "supervisor")
  supervisor ! FailChild
  supervisor ! CheckChild

}
