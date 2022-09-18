package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ChildActorOfficialSolution extends App {

  object WordCounterMaster {
    case class Initialize(nChildren: Int)
    case class WordCountTask(id:Int,text: String)
    case class WordCountReply(id: Int,count: Int)
  }

  class WordCounterMaster extends Actor {
    import WordCounterMaster._
    override def receive: Receive = {
      case Initialize(nChildren:Int) =>
        println("[Master] initializing ...")
        val childrenRefs = for (i <- 1 to nChildren) yield context.actorOf(Props[WorldCounterWorker], s"wsw_$i")
        context.become(withChildren(childrenRefs, 0, 0, Map()))
    }

    def withChildren(childrenRefs: Seq[ActorRef], parentChildIndex:Int, currentTaskId: Int, requestMap: Map[Int, ActorRef]): Receive = {
      case text:String =>
        println(s"[master] I have  received $text to the children $parentChildIndex ")
        val originalSender = sender()
        val task = WordCountTask(currentTaskId, text)
        val childRef = childrenRefs(parentChildIndex)
        childRef ! task
        val nextIndex = (parentChildIndex+1)%childrenRefs.size
        val newTaskId = currentTaskId + 1
        val newRequestMap = requestMap + (currentTaskId -> originalSender)
        context.become(withChildren(childrenRefs, nextIndex, newTaskId, newRequestMap))
      case WordCountReply(id, count) =>
        println(s"[master] I have received reply for task id $id ")
        val sender = requestMap(id)
        sender ! count
        context.become(withChildren(childrenRefs, parentChildIndex, currentTaskId, requestMap - id))
    }
  }

  class WorldCounterWorker extends Actor {
    import WordCounterMaster._
    override def receive: Receive = {
      case WordCountTask(id, message) =>
        println(s"${self.path} I have received task $id with $message")
        sender() ! WordCountReply(id, message.split(" ").length)
    }
  }

  class TestActor extends Actor {
    import WordCounterMaster._
    override def receive: Receive = {
      case "go" =>
        val master = context.actorOf(Props[WordCounterMaster], "master")
        master ! Initialize(3)
        val texts = List(" I love programming", "yes", "No you")
        texts.foreach(text => master ! text)
      case  count: Int  =>
        println(s"[texst actor] I have  received reply $count")
    }
  }

  val system = ActorSystem("roundRobin")
  val testActor = system.actorOf(Props[TestActor], "testActor")
  testActor ! "go"

}
