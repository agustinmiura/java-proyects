package part2actors

import akka.actor.{Actor, ActorSystem, Props}

object ActorCapabilities extends App {

  case class SpecialMessage(contents: String)

  class SimpleActor extends Actor {
     override def receive(): Receive = {
       case message: String => println(s"[SimpleActor] I have received $message")
       case number: Int => println(s"[SimpleActor] I have received a number  $number")
       case specialMessage: SpecialMessage => println(s"[SimpleActor] specialMessage $specialMessage")
     }
  }

  val system = ActorSystem("actorCapacityDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")
  simpleActor ! "Hello, actor"
  simpleActor ! SpecialMessage("A special message")

}
