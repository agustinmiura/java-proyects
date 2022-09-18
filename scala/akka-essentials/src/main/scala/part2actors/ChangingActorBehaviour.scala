package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ChangingActorBehaviour.Mom.MomStart

object ChangingActorBehaviour extends App {

  object FuzzyKid {
    case object KidAccept
    case object KidReject
    val HAPPY = "HAPPY"
    val SAD = "SAD"
  }

  class FussyKid extends Actor {
    import FuzzyKid._
    import Mom._
    var state = HAPPY
    override def receive: Receive = {
      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(_) =>
        if (state == HAPPY) sender() ! KidAccept
        else sender() ! KidReject
    }
  }

  object Mom {
    case class MomStart(kidReference: ActorRef)
    case class Food(food: String)
    case class Ask(message: String)
    val VEGETABLE = "V"
    val CHOCOLATE = "C"
  }

  class Mom extends Actor {
    import Mom._
    import  FuzzyKid._
    override def receive: Receive = {
      case MomStart(kidRef) =>
        kidRef ! Food(VEGETABLE)
        kidRef ! Food(VEGETABLE)
        kidRef ! Food(CHOCOLATE)
        kidRef ! Food(CHOCOLATE)
        kidRef ! Ask("Do you want to play")
      case KidAccept => println("Yeah my kid is happy")
      case KidReject => println("Kid sad")
    }
  }

  val system = ActorSystem("changingActorBehaviour")
  val fuzzyKid = system.actorOf(Props[FussyKid])
  val statelessFussyKid = system.actorOf(Props[StatelessFussyKid])
  val mom = system.actorOf(Props[Mom])

  //mom ! MomStart(fuzzyKid)

  class StatelessFussyKid extends Actor {
    import Mom._
    import FuzzyKid._

    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(VEGETABLE)  => context.become(sadReceive, false)
      case Food(CHOCOLATE) =>
      case Ask(_) => sender() ! KidAccept
    }
    def sadReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive, false)
      case Food(CHOCOLATE) => context.unbecome()
      case Ask(_) => sender() ! KidReject
    }

  }

  mom ! MomStart(statelessFussyKid)

}
