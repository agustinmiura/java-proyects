package part2actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object IntroAkkaConfig extends App {

  class SimpleLoggingActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }

  val configString =
    """
      |akka {
      | loglevel = "INFO"
      |}
      |""".stripMargin

  val config = ConfigFactory.parseString(configString)
  val system = ActorSystem("ConfigurationDemo", ConfigFactory.load(config))
  val actor = system.actorOf(Props[SimpleLoggingActor])

  actor ! "A message to remember"

  val defaultConfig = ActorSystem("DefaultConfigFileDemo")
  val defaultConfigActor = defaultConfig.actorOf(Props[SimpleLoggingActor])
  defaultConfigActor ! "Another message"

  val specialConfigObject = ConfigFactory.load().getConfig("mySpecialConfig")
  val specialConfigSystem= ActorSystem("SpecialConfigDemo", specialConfigObject)
  val anotherActor = specialConfigSystem.actorOf(Props[SimpleLoggingActor])
  anotherActor ! "Send last message"

  val separatedConfig = ConfigFactory.load("secretFolder/secretConfiguration.conf")
  //println(s" ${separatedConfig} value : ${separatedConfig.getString("akka.loglevel}")}")

  val jsonConfig = ConfigFactory.load("jsonConfig.json")
  println(s"json config: ${jsonConfig}")

  val propsConfig = ConfigFactory.load("props/config.properties")
  println(s" I see the propsConfig ${propsConfig.getString("akka.loglevel")} ")
}
