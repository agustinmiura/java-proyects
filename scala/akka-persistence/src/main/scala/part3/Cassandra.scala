package part3

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Cassandra extends App {
  val localStoresActorSystem = ActorSystem("cassandra", ConfigFactory.load().getConfig("cassandraDemo"))
  val persistantActor = localStoresActorSystem.actorOf(Props[SimplePersistanceActor], "simplePersistantActor")
  for (i <- 1 to 10) {
    persistantActor ! s"I love Akka $i"
  }
  persistantActor ! "print"
  persistantActor ! "snap"
  for (i <- 11 to 20) {
    persistantActor ! s"I love Akka[$i]"
  }
}
