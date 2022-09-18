package part2actors

import akka.actor.{Actor, ActorSystem, Props}

object ActorsIntro extends App {

  val actorSystem = ActorSystem("HelloActorSystem")
  println(actorSystem.name)

  class WordCountActor extends Actor {
    var totalWords = 0
    override def receive(): Receive = {
      case message: String =>
        println(s"[wordCoiunt] I have receoved: $message")
        totalWords += message.split(" ").length
      case msg => println(s"s[word counter] I cannot undestand ${msg.toString}")
    }
  }

  val wordCounter = actorSystem.actorOf(Props[WordCountActor], "wordCounter")
  val anotherWordCounter = actorSystem.actorOf(Props[WordCountActor], "anotherWordCounter")

  wordCounter ! "I am learning Akka and is pretty damn cool"
  anotherWordCounter ! " Antoher message "

  object Person {
    def props(name: String) = Props(new Person(name))
  }

  class Person(name: String) extends Actor {
    override def receive(): Receive = {
      case "Hi" => println(s"Hi, my name is $name")
      case _ => ""
    }
  }
  val person = actorSystem.actorOf(Person.props("JohnDoe"))

  person ! "Hi"

}
