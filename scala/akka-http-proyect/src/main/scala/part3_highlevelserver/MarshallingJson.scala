package part3_highlevelserver

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import part3_highlevelserver.GameAreMap._
// step 1
import spray.json._

import scala.concurrent.duration.DurationInt
// step 1

case class Player(nickName: String, characterClass: String, level: Int)

object GameAreMap {
  case object GetAllPlayers

  case class GetPlayer(nickname: String)

  case class GetPlayerByClass(characterClass: String)

  case class AddPlayer(player: Player)

  case class RemovePlayer(player: Player)

  case object OperationSuccess
}

class GameArea extends Actor with ActorLogging {

  import GameAreMap._

  var players = Map[String, Player]()

  override def receive: Receive = {
    case GetAllPlayers =>
      log.info("Getting all players")
      sender() ! players.values.toList
    case GetPlayer(nickname) =>
      log.info(s"Getting player with nickname $nickname")
      sender() ! players.get(nickname)
    case GetPlayerByClass(characterClass) =>
      log.info(s"Getting all players by character class $characterClass")
      sender() ! players.values.toList.filter(_.characterClass == characterClass)
    case AddPlayer(player) =>
      log.info(s"Trying to add player $player")
      players = players + (player.nickName -> player)
      sender() ! OperationSuccess
    case RemovePlayer(player) =>
      log.info(s"Trying to remove a player $player")
      players = players - player.nickName
  }
}

trait PlayerJsonProtocol extends DefaultJsonProtocol {
  implicit val playerFormat = jsonFormat3(Player)
}

object MarshallingJson extends App with PlayerJsonProtocol with SprayJsonSupport {
  implicit val system = ActorSystem("HighLevelIntro")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val playerSystem = system.actorOf(Props[GameArea], "rockTheJvmAreaMap")

  val playersList = List(
    Player("warriorplayer", "Warrior", 70),
    Player("elfPlayer", "Elf", 67),
    Player("danielPlayer", "Wizard", 30)
  )

  playersList.foreach { player =>
    playerSystem ! AddPlayer(player)
  }

  implicit val timeout = Timeout(2 seconds)
  val rockTheJvmRoute = pathPrefix("api" / "player") {
    get {
      path("class" / Segment) { characterclass =>
        val playersByClassFutureMap = (playerSystem ? GetPlayerByClass(characterclass)).mapTo[List[Player]]
        complete(playersByClassFutureMap)
      }
    } ~
      (path(Segment) | parameter('nickname)) { nickname =>
        val playerOptionFuture = (playerSystem ? GetPlayer(nickname)).mapTo[Option[Player]]
        complete(playerOptionFuture)
      } ~
      pathEndOrSingleSlash {
        complete((playerSystem ? GetAllPlayers).mapTo[List[Player]])
      } ~
      post {
        entity(implicitly[FromRequestUnmarshaller[Player]]) { player =>
          complete((playerSystem ? AddPlayer(player)).map(_ => StatusCodes.OK))
        }
      } ~
      delete {
        entity(as[Player]) { player =>
          complete((playerSystem ? RemovePlayer(player)).map(_ => StatusCodes.OK))
        }
      }
  }

  Http().bindAndHandle(rockTheJvmRoute, "localhost", 8080)

}
