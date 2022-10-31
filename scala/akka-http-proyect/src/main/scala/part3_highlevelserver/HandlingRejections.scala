package part3_highlevelserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{MethodRejection, MissingQueryParamRejection, Rejection, RejectionHandler}
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.duration._

object HandlingRejections extends App {

  implicit val system = ActorSystem("HandlingRejections")
  implicit val materializer = ActorMaterializer()
  implicit val defaultTimeout = Timeout(2 seconds)

  val simpleRoute =
    path("api" / "myEndpoint") {
      get {
        complete(StatusCodes.OK)
      } ~
        parameter('id) { _ =>
          complete(StatusCodes.OK)
        }
    }

  val badRequestHandler: RejectionHandler = { rejections: Seq[Rejection] =>
    println(s"I have encountered rejections: $rejections")
    Some(complete(StatusCodes.BadRequest))
  }

  val forbiddenHandler: RejectionHandler = { rejections: Seq[Rejection] =>
    println(s"I have encountered rejections: $rejections")
    Some(complete(StatusCodes.Forbidden))
  }

  val simpleRouteWithHandlers = handleRejections(badRequestHandler) {
    path("api" / "myEndpoint") {
      get {
        complete(StatusCodes.OK)
      }
    } ~
      post {
        handleRejections(forbiddenHandler) {
          parameter('myParam) { myParam =>
            complete(StatusCodes.OK)
          }
        }
      }
  }

  Http().bindAndHandle(simpleRouteWithHandlers, "localhost", 8080)

  implicit val customRejectionHandler = RejectionHandler.newBuilder()
    .handle {
      case m: MethodRejection =>
        println(s"I got a method rejection: $m")
        complete("Rejected method!")
    }
    .result()

  Http().bindAndHandle(simpleRoute, "localhost", 9090)
}
