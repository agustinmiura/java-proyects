package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ChildActorsExerciseWithRandomBalance.WordCounterMaster.{Initialize, WordCountTask}

import scala.util.Random

/**
 * Solution to the exercise choosing a random worker
 */
object ChildActorsExerciseWithRandomBalance extends App {

  val system = ActorSystem("childExercise")

  object WordCounterMaster {
    case class Initialize(nChildren: Int)
    case class WordCountTask(text: String)
    case class WorldCountReply(count: Int)
  }

  val random = Random
  class WordCounterMaster extends Actor {
    var workers:List[ActorRef] = List()
    import WordCounterMaster._
    override def receive: Receive = {
      case Initialize(nChildren: Int) =>
        val range = Range.inclusive(0, nChildren-1)
        range.foreach(index => {
          val childRef = context.actorOf(Props[WordCounter], s"child${index}")
          println(s"Created ${childRef.path}")
          workers = childRef +: workers
        })
      case WordCountTask(text: String) =>
        val size = workers.size
        val index = random.nextInt(size)
        val name = s"child${index}"
        val actor = system.actorSelection(s"/user/masterActor/${name}")
        actor ! WordCountTask(text)
        println(s"[WordCounterMaster] WordCountTask ${text} sending to actor ${name}")
      case WorldCountReply(count) =>
        println(s"[WordCounterMaster] WorldCountReply ${count} from sender ${sender()}")
    }
  }

  class WordCounter extends Actor {
    import WordCounterMaster._
    override def receive: Receive = {
      case WordCountTask(text) =>
        println(s"[WordCounter] receive $text")
        val wordQty = text.split(" ").length
        sender() ! WorldCountReply(wordQty)
      case _ => println(s"[WordCounter] Not matching")
    }
  }

  def generateWord(): String = {
    val randomNumber = random.nextInt(30)
    val sampleWord: String = Range.inclusive(0, randomNumber).mkString(" ")
    sampleWord
  }

  val masterActor = system.actorOf(Props[WordCounterMaster], "masterActor")
  masterActor ! Initialize(10)
  Thread.sleep(1000)
  Range(0,20).foreach(index => {
    val word = generateWord()
    masterActor ! WordCountTask(word)
  })

  /**
   * create a word count master
   * send initialize 10 to the master
   *  the master will create
   * send "A word" to the master
   *  the master will send task to one of its children
   *    child replies with qty here
   *  master replices with 3 to the sender
   *
   *  r -> wcm -> wcw
   *  r  <-  wcm  <-
   *
   *
   */


}
