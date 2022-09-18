package part3testing

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import part3testing.BasicSpec.{BlackHole, LabTestActor, SimpleActor}

import scala.concurrent.duration._
import scala.util.Random

class BasicSpec extends TestKit(ActorSystem("basicSpec"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A simple actor" should {
    "send back the same message in" in {
      val echoActor = system.actorOf(Props[SimpleActor])
      val message = "hello, test"
      echoActor ! message
      expectMsg(message)
    }
  }

  "A blackhole actor" should {
    "send back some message" in {
      val blackhole = system.actorOf(Props[BlackHole])
      val message = "hello, test"
      blackhole ! message
      expectNoMessage(1 second)
    }
  }

  "A lab test actor" should {
    val testLabActor = system.actorOf(Props[LabTestActor])
    "turn a string  uppercase" in {
      testLabActor ! "I love akka"
      val reply = expectMsgType[String]
      assert(reply == "I LOVE AKKA")
    }
    "reply to a greeting" in {
      testLabActor ! "greeting"
      expectMsgAnyOf("hi", "hello")
    }
    "reply with favourite tech" in {
      testLabActor ! "favouriteTech"
      expectMsgAllOf("Scala", "Akka")
    }
    "reply with cool tech  in a different way" in {
      testLabActor ! "favouriteTech"
      val messages = receiveN(2)
    }
    "reply with cool tech in a fancy way" in {
      testLabActor ! "favouriteTech"
      expectMsgPF() {
        case "Scala" =>
        case "Akka" =>
      }
    }
  }

}

object BasicSpec {

  class SimpleActor extends Actor {
    override def receive: Receive = {
      case message => sender() ! message
    }
  }

  class BlackHole extends Actor {
    override def receive: Receive = Actor.emptyBehavior
  }

  class LabTestActor extends Actor {
    val random = new Random()

    override def receive: Receive = {
      case "favouriteTech" =>
        sender() ! "Scala"
        sender() ! "Akka"
      case "greeting" =>
        if (random.nextBoolean()) sender() ! "hi" else "hello"
      case message: String => sender() ! message.toUpperCase()
    }
  }
}