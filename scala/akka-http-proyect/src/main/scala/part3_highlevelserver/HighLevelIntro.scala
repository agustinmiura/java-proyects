package part3_highlevelserver

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.{Http, HttpConnectionContext}
import akka.stream.ActorMaterializer

object HighLevelIntro extends App {

  import system.dispatcher

  implicit val system = ActorSystem("HighLevelIntro")
  implicit val materrializer = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._

  val simpleRoute: Route = path("home") {
    complete(StatusCodes.OK)
  }

  val pathGetRoute: Route =
    path("home") {
      get {
        complete(StatusCodes.OK)
      }
    }

  val chainedRoute: Route =
    path("myEndpoint") {
      get {
        complete(StatusCodes.OK)
      } ~
        post {
          complete(StatusCodes.Forbidden)
        } ~ path("home") {
        complete(
          HttpEntity(
            ContentTypes.`text/xml(UTF-8)`,
            """
              |<html></html>
              |""".stripMargin
          )
        )
      }
    }
  Http().bindAndHandle(pathGetRoute, "localhost", 8080, HttpConnectionContext)

}
