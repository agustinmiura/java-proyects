package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ChildActorOfficialSolution extends App {

  val system = ActorSystem("childExercise")

  object WordCounterMaster {
    case class Initialize(nChildren: Int)
    case class WordCountTask(id:Int,text: String)
    case class WordCountReply(id: Int,count: Int)
  }

  class WordCounterMaster extends Actor {
    import WordCounterMaster._
    override def receive: Receive = {
      case Initialize(nChildren:Int) =>
        val childrenRefs = for (i <- 1 to nChildren) yield context.actorOf(Props[WorldCounterWorker], s"wsw_$i")
        context.become(withChildren(childrenRefs, 0, 0, Map()))
    }

    def withChildren(childrenRefs: Seq[ActorRef], parentChildIndex:Int, currentTaskId: Int, requestMap: Map[Int, ActorRef]): Receive = {
      case text:String =>
        val originalSender = sender()
        val task = WordCountTask(currentTaskId, text)
        val childRef = childrenRefs(parentChildIndex)
        childRef ! task
        val nextIndex = (parentChildIndex+1)%childrenRefs.size
        val newTaskId = currentTaskId + 1
        val newRequestMap = requestMap + (currentTaskId, originalSender)
        context.become(withChildren(childrenRefs, nextIndex, newTaskId, newRequestMap))
      case WordCountReply(id, count) =>
        val sender = requestMap(id)
        sender ! count
        context.become(withChildren(childrenRefs, parentChildIndex, currentTaskId, requestMap - id))
    }
  }

  class WorldCounterWorker extends Actor {
    import WordCounterMaster._
    override def receive: Receive = {
      case WordCountTask(id, message) =>
        sender() ! WordCountReply(id, message.split(" ").length)
    }
  }

}
