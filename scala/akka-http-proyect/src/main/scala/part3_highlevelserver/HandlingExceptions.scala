package part3_highlevelserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer

object HandlingExceptions extends App {

  implicit val system = ActorSystem("HandlingExceptions")
  implicit val materializer = ActorMaterializer()

  val simpleRoute =
    path("api" / "people") {
      get {
        throw new RuntimeException("Getting all the people took too long")
      } ~
        handleExceptions(noSuchElementHandler) {
          post {
            parameter("id") { id =>
              if (id.length > 2) {
                throw new RuntimeException(s"Parameter $id cannot be found in the db")
              }
              complete(StatusCodes.OK)
            }
          }
        }
    }

  implicit val customExceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: RuntimeException =>
      complete(StatusCodes.NotFound, e.getMessage)
    case e: IllegalArgumentException =>
      complete(StatusCodes.BadRequest, e.getMessage)

  }

  Http().bindAndHandle(simpleRoute, "localhost", 9090)

  val runtimeExceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: RuntimeException =>
      complete(StatusCodes.NotFound, e.getMessage)
  }

  val noSuchElementHandler: ExceptionHandler = ExceptionHandler {
    case e: RuntimeException =>
      complete(StatusCodes.BadRequest, e.getMessage)
  }

  val delicateHandleRoute = path("api" / "people") {
    handleExceptions(runtimeExceptionHandler)
    get {
      throw new RuntimeException("Getting all the people took too long")
    } ~
      post {
        parameter("id") { id =>
          if (id.length > 2) {
            throw new RuntimeException(s"Parameter $id cannot be found in the db")
          }
          complete(StatusCodes.OK)
        }
      }
  }

  Http().bindAndHandle(delicateHandleRoute, "localhost", 8080)

}
