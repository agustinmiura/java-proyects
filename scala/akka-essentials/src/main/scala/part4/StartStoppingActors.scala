package part4

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Kill, PoisonPill, Props, Terminated}
import part4.StartStoppingActors.Parent.StartChild

object StartStoppingActors extends App {

  val system = ActorSystem("stoppingActorsDemo")

  object Parent {
    case class StartChild(name: String)

    case class StopChild(name: String)

    case object Stop
  }

  class Parent extends Actor with ActorLogging {

    import Parent._

    override def receive: Receive = withChildren(Map())

    def withChildren(children: Map[String, ActorRef]): Receive = {
      case StartChild(name) =>
        log.info(s"Starting child $name")
        val child = context.actorOf(Props[Child], name)
        context.become(withChildren(children + (name -> child)))
      case StopChild(name) =>
        log.info(s"Stopping child $name")
        val childOption = children.get(name)
        childOption.foreach(childRef => context.stop(childRef))
      case Stop =>
        log.info("Stopping myself")
        context.stop(self)
      case message => log.info(message.toString)
    }
  }

  class Child extends Actor with ActorLogging {
    override def receive: Receive = {
      case message: String => log.info(message)
    }
  }

  /*
  val parent = system.actorOf(Props[Parent], "parent")
  parent ! StartChild("child1")
  val child = system.actorSelection("/user/parent/child1")
  child ! "Hi kid"
  parent ! StopChild("child1")
  for (_ <- 1 to 50) child ! "Are you still there?"
  parent ! StartChild("child2")

  val child2 = system.actorSelection("/user/parent/child1")
  child2 ! "Hi"
  parent ! Stop
  for (_ <- 1 to 10) parent ! "parent are you still there?"
  for (i <- 1 to 100) child2 ! s"[$i] second kid, are you still alive?"
  */

  val looseActor = system.actorOf(Props[Child])
  looseActor ! "Hello"
  looseActor ! PoisonPill
  looseActor ! "Stil there?"

  val abruptlyTerminatedActor = system.actorOf(Props[Child])
  abruptlyTerminatedActor ! "About to be terminated"
  abruptlyTerminatedActor ! Kill

  class Watcher extends Actor with ActorLogging {

    import Parent._

    override def receive: Receive = {
      case StartChild(name) =>
        val child = context.actorOf(Props[Child], name)
        log.info(s"Started and watching child $name")
        context.watch(child)
      case Terminated(ref) =>
        log.info(s"The reference I am watching  $ref has been stopped")
    }
  }

  val watcher = system.actorOf(Props[Watcher], "watcher")
  watcher ! StartChild("watchedChild")
  val watchedChild = system.actorSelection("/user/watcher/watchedChild")
  Thread.sleep(1000)
  watchedChild ! PoisonPill

}
