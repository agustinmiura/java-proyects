package part3

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.PersistentActor
import akka.serialization.Serializer
import com.typesafe.config.ConfigFactory

case class RegisterUser(email: String, name: String)

case class UserRegistered(id: Int, email: String, name: String)

class UserRegistrationSerializer extends Serializer {
  override def identifier: Int = 23321123
  override def toBinary(o:AnyRef): Array[Byte] = o match {
    case event @ UserRegistered(id, email, name) =>
      println(s"Serializing an event")
      s"[$id//$email//$name]".getBytes()
    case _ =>
      throw new IllegalArgumentException("Illegal class for this class")
  }
  def fromBinary(bytes: Array[Byte], manifest: Option[Class[_]]): AnyRef = {
    val string = new String(bytes)
    val values = string.substring(1, string.length() -1).split("//")
    val id = values(0).toInt
    val email = values(1)
    val name = values(2)
    val result = UserRegistered(id, email, name)
    println(s"Deserialized : $result")
    result
  }
  override def includeManifest: Boolean = false
}

class UserRegistrationActor extends PersistentActor with ActorLogging {
  var currentId = 0

  override def persistenceId: String = "user-registration"

  override def receiveCommand: Receive = {
    case RegisterUser(email, name) =>
      persist(UserRegistered(currentId, email, name)) { event =>
        currentId += 1
        log.info(s"Persisted: $event")
      }
  }

  override def receiveRecover: Receive = {
    case event@UserRegistered(id, _, _) =>
      log.info(s"Recovered: $event")
      currentId = id
  }

}

object CustomSerialization extends App {
  val actorSystem = ActorSystem("CustomSerialization", ConfigFactory.load().getConfig("customSerializerDemo"))
  val userRegistration = actorSystem.actorOf(Props[UserRegistrationActor], "userRegistration")
  for(i <- 1 to 10) {
    userRegistration ! RegisterUser(s"user_$i@gmail.com", s"User $i")
  }
}
