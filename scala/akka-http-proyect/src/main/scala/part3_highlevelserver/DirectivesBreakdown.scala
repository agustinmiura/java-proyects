package part3_highlevelserver

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, StatusCodes}
import akka.stream.ActorMaterializer

object DirectivesBreakdown extends App {

  implicit val system = ActorSystem("DirectivesBreakdown")
  implicit val materializer = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._

  val simpleHttpRoute = post {
    complete(StatusCodes.Forbidden)
  }

  val simplePathRoute = path("about") {
    complete {
      HttpEntity(
        ContentTypes.`application/json`,
        """
          |<html>
          |<body>Hello message!!</body>
          |</html>
          |""".stripMargin
      )
    }
  }

  val complexRoute = path("api" / "myEndpoint") {
    complete(StatusCodes.OK)
  }

  val dontConfuse = path("api/myEndpoint") {
    complete(StatusCodes.OK)
  }

  val pathEndRoute = pathEndOrSingleSlash {
    complete(StatusCodes.OK)
  }

  val pathExtractionRoute = path("api" / "item" / IntNumber) { (itemNumber: Int) =>
    println(s"I've got a number in my path $itemNumber")
    complete(StatusCodes.OK)
  }

  val pathMultiExtractRoute = path("api" / "order" / IntNumber / IntNumber) { (id, inventory) =>
    println(s"Ive got two numbers $id, $inventory")
    complete(StatusCodes.OK)
  }

  val queryParamExtractionRoute =
    path("api" / "item") {
      parameter('id.as[Int]) { (itemId: Int) =>
        println(s"I've extracted item id : $itemId")
        complete(StatusCodes.OK)
      }
    }

  val extractRequest = path("controlEndpoint") {
    extractRequest { httpRequest: HttpRequest =>
      println(s"I've got the http request as $httpRequest")
      complete(StatusCodes.OK)
    }
  }

  Http().bindAndHandle(queryParamExtractionRoute, "localhost", 8080)
}
