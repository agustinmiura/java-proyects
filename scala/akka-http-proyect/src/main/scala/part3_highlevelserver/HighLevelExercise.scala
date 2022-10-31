package part3_highlevelserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import spray.json._

import scala.concurrent.duration._
import scala.util.{Failure, Success}

case class Person(pin: Int, name: String)

trait PersonJsonProtocol extends DefaultJsonProtocol {
  implicit val personJson = jsonFormat2(Person)
}

object HighLevelExercise extends App with PersonJsonProtocol {

  implicit val system = ActorSystem("HighLevelExample")
  implicit val materializer = ActorMaterializer()
  implicit val defaultTimeout = Timeout(2 seconds)

  import system.dispatcher

  var people = List(
    Person(1, "Alice"),
    Person(2, "Bob"),
    Person(3, "Charlie")
  )

  println(people.toJson.prettyPrint)

  val personServerRoute = pathPrefix("api" / "people") {
    get {
      (path(IntNumber) | parameter('pin.as[Int])) { pin =>
        complete(HttpEntity(
          ContentTypes.`application/json`,
          people.find(_.pin == pin).headOption.toJson.prettyPrint
        ))
      }
    } ~
      pathEndOrSingleSlash {
        complete(HttpEntity(
          ContentTypes.`application/json`,
          people.toJson.prettyPrint
        ))
      }
    (post & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) =>
      val entity = request.entity
      val strictEntityFuture = entity.toStrict(2 seconds)
      val personFuture = strictEntityFuture.map { strictEntity => strictEntity.data.utf8String.parseJson.convertTo[Person]}
        personFuture.onComplete {
          case Success(person) =>
            log.info(s"Got person: $person")
            people = people :+ person
          case Failure(ex) =>
            log.warning("Something failed with fetching the person from the entity")
        }
        complete(personFuture.map(_ => StatusCodes.OK).recover {
          case _ => StatusCodes.InternalServerError
        }
      )
    }
  }

  Http().bindAndHandle(personServerRoute, "localhost", 8080)

}
