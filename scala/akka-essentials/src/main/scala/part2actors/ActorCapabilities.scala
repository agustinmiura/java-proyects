package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ActorCapabilities extends App {

  case class SpecialMessage(contents: String)

  class SimpleActor extends Actor {
     override def receive(): Receive = {
       case "Hi!" => context.sender() ! "Hello, There!"
       case message: String => println(s"[$self] I have received $message")
       case number: Int => println(s"[SimpleActor] I have received a number  $number")
       case specialMessage: SpecialMessage => println(s"[SimpleActor] specialMessage $specialMessage")
       case messagetoYourself: SendMessagetoYourself =>
        self ! messagetoYourself
       case SayHiTo(ref) => ref ! "Hi!"
       case WirelessPhoneMessage(content, ref) => ref forward (content + "forwarded")
     }
  }

  val system = ActorSystem("actorCapacityDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")
  simpleActor ! "Hello, actor"
  simpleActor ! SpecialMessage("A special message")

  case class SendMessagetoYourself(content:String)
  simpleActor ! SendMessagetoYourself(" I am talkiong ")

  case class SayHiTo(ref: ActorRef)

  val alice = system.actorOf(Props[SimpleActor], "alice")
  val bob = system.actorOf(Props[SimpleActor], "bob")

  alice ! SayHiTo(bob)
  alice ! "Hi!"

  case class WirelessPhoneMessage(content:String, ref: ActorRef)
  alice ! WirelessPhoneMessage("Hi", bob)


}
