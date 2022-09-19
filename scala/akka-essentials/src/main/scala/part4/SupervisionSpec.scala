package part4

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, AllForOneStrategy, OneForOneStrategy, Props, SupervisorStrategy, Terminated}
import akka.testkit.{EventFilter, ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class SupervisionSpec extends TestKit(ActorSystem("supervisionSpec"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll  {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
  import SupervisionSpec._

  "A supervisor" should  {
    "resume its child in csae of a minor fault" in {
      val supervisor = system.actorOf(Props[Supervisor])
      supervisor ! Props[FussyWordCounter]
      val child = expectMsgType[ActorRef]

      child ! "I love programming"
      child ! Report
      expectMsg(3)

      child ! "This is a long sentence because I am trying to do something"
      child ! Report
      expectMsg(3)
    }

    "restart its child in case of NPE" in {
      val supervisor = system.actorOf(Props[Supervisor])
      supervisor ! Props[FussyWordCounter]
      val child = expectMsgType[ActorRef]
      child ! "I love programming"
      child ! Report
      expectMsg(3)

      child ! ""
      child ! Report
      expectMsg(0)
    }

    "terminate a child in case of a major error" in {
      val supervisor = system.actorOf(Props[Supervisor])
      supervisor ! Props[FussyWordCounter]
      val child = expectMsgType[ActorRef]

      watch(child)
      child ! "stop now child"
      val terminatedMessage = expectMsgType[Terminated]
      assert(terminatedMessage.actor == child)
    }
    "escalate an error with unknown" in {
      val supervisor = system.actorOf(Props[Supervisor], "supervisor")
      supervisor ! Props[FussyWordCounter]
      val child = expectMsgType[ActorRef]

      watch(child)
      child ! 99
      val terminatedMessage = expectMsgType[Terminated]
      assert(terminatedMessage.actor == child)

    }
    "A kind supervisor" should {
      "not kill children in case its restarted " in {
        val supervisor = system.actorOf(Props[NoDeathOnRestartSupervisor], "supervisor")
        supervisor ! Props[FussyWordCounter]
        val child = expectMsgType[ActorRef]

        child ! "Akka is cool"
        child ! Report
        expectMsg(3)

        child ! 45
        child ! Report
        expectMsg(0)
      }
    }
    "An all-for-one supervisor" should {
      "apply the all-for-one strategy" in {
        val supervisor = system.actorOf(Props[AllForOneSupervisor], "allForOneSupervisor")
        supervisor ! Props[FussyWordCounter]
        val child = expectMsgType[ActorRef]

        supervisor ! Props[FussyWordCounter]
        val secondChild = expectMsgType[ActorRef]

        secondChild ! "Testing supervision"
        secondChild ! Report
        expectMsg(2)

        EventFilter[NullPointerException]() intercept {
          child ! ""
        }

        Thread.sleep(500)

        secondChild ! Report
        expectMsg(0)
      }
    }

  }
}

object SupervisionSpec {



  class Supervisor extends Actor {

    override val supervisorStrategy:SupervisorStrategy = OneForOneStrategy() {
      case _: NullPointerException => Restart
      case _: IllegalArgumentException => Stop
      case _: RuntimeException => Resume
      case _: Exception => Escalate
    }

    override def receive: Receive = {
      case props: Props =>
        val actorRef = context.actorOf(props)
        sender() ! actorRef
    }
  }

  class NoDeathOnRestartSupervisor extends Supervisor with ActorLogging {
    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
      log.info("No restarting")
    }
  }

  class AllForOneSupervisor extends Supervisor {
    override val supervisorStrategy = AllForOneStrategy() {
      case _: NullPointerException => Restart
      case _: IllegalArgumentException => Stop
      case _: RuntimeException => Resume
      case _: Exception => Escalate
    }
  }

  case object Report
  class FussyWordCounter extends Actor {
    var words = 0
    override def receive: Receive = {
      case "" => throw new NullPointerException("Sentence is empty")
      case sentence: String =>
        if (sentence.length>20) throw new RuntimeException("Sentence is too big")
        else if (!Character.isUpperCase(sentence(0))) throw new IllegalArgumentException("Sentence must start with uppercases")
        else words += sentence.split(" ").length
      case Report => sender() ! words
      case _ => throw new Exception("Can only receive strings")
    }
  }



}

