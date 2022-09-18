package part3testing

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{CallingThreadDispatcher, TestActorRef, TestProbe}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import part3testing.SyncTestSpec.{Counter, Inc, Read}

import scala.concurrent.duration.Duration

class SyncTestSpec extends WordSpecLike with BeforeAndAfterAll {

  implicit val system = ActorSystem("syncTestingSpec")
  override def afterAll(): Unit = {
    system.terminate()
  }

  "A counter" should  {
    "synchronously increase its counter" in {
      val counter = TestActorRef[Counter](Props[Counter])
      counter ! Inc // counter has ALREADY received the message
      assert(counter.underlyingActor.count == 1)
    }
    "work in the calling thread dispatcher" in {
      val counter = system.actorOf(Props[Counter].withDispatcher(CallingThreadDispatcher.Id))
      val probe = TestProbe()

      probe.send(counter, Read)
      probe.expectMsg(Duration.Zero, 0)
    }
  }

}

object SyncTestSpec {
  case object Inc
  case object Read
  class Counter extends Actor {
    var count = 0
    override def receive: Receive = {
      case Inc => count += 1
      case Read => sender() ! count
    }
  }
}
