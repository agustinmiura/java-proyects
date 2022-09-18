package part2actors

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}

object ChildActors extends App {

  object ParentActor {
    case class CreateChild(name: String)

    case class TellChild(message: String)
  }

  class ParentActor extends Actor {

    import ParentActor._

    override def receive: Receive = {
      case CreateChild(name) =>
        println(s"[ParentActor] ${self.path} creating child")
        val childRef = context.actorOf(Props[ChildActor], name)
        context.become(withChild(childRef))
    }

    def withChild(childRef: ActorRef): Receive = {
      case TellChild(message) => if (childRef != null) childRef forward message
    }
  }

  class ChildActor extends Actor {
    override def receive: Receive = {
      case message => println(s"${self.path} I got the message $message")
      case _ => println("[ChildActor] No matching")
    }
  }

  val system = ActorSystem("ParentChildDemo")
  import ParentActor._
  val parentActor = system.actorOf(Props[ParentActor], "parentActor")
  parentActor ! CreateChild("child")
  parentActor ! TellChild("Hello kid")

  val childSelection: ActorSelection = system.actorSelection("/user/parentActor/child")
  childSelection ! "I found you"

}
