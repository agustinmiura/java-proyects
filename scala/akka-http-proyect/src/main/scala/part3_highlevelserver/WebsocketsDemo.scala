package part3_highlevelserver


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.{CompactByteString, Timeout}

import scala.concurrent.duration._

object WebsocketsDemo extends App {

  implicit val system = ActorSystem("Websockets")
  implicit val materializer = ActorMaterializer()
  implicit val defaultTimeout = Timeout(2 seconds)

  val textMessage = TextMessage(Source.single("hello via text message"))
  val binaryMessage = BinaryMessage(Source.single(CompactByteString("Hello via binary message")))

  val html =
    """
      |<html>
      |    <head>
      |        <script>
      |            var exampleSocket = new WebSocket("ws://localhost:8080/greeter");
      |            console.log("starting websocket...");
      |
      |            exampleSocket.onmessage = function(event) {
      |                var newChild = document.createElement("div");
      |                newChild.innerText = event.data;
      |                document.getElementById("1").appendChild(newChild);
      |            };
      |
      |            exampleSocket.onopen = function(event) {
      |                exampleSocket.send("socket seems to be open...");
      |            };
      |
      |            exampleSocket.send("socket says: hello, server!");
      |        </script>
      |    </head>
      |
      |    <body>
      |        Starting websocket...
      |        <div id="1">
      |        </div>
      |    </body>
      |
      |</html>
      |""".stripMargin

  def websocketFlow: Flow[Message, Message, Any] = Flow[Message].map {
    case textMessage: TextMessage =>
      TextMessage(Source.single("Server says back") ++ textMessage.textStream ++ Source.single("!"))
    case binaryMessage: BinaryMessage =>
      binaryMessage.dataStream.runWith(Sink.ignore)
      TextMessage(Source.single("Server received a binary message..."))
  }

  val websocketRoute =
    (pathEndOrSingleSlash & get) {
      complete(HttpEntity(
        ContentTypes.`application/json`,
        html
      ))
    } ~
      path("greeter") {
        handleWebSocketMessages(socialFlow)
      }

  Http().bindAndHandle(websocketRoute, "localhost", 8080)

  case class SocialPost(owner: String, content: String)

  val socialFeed = Source(
    List(
      SocialPost("Martin", "Scala 4 has been announced"),
      SocialPost("Daniel", "A book has been published"),
      SocialPost("Martin", "I have killed PHP")
    )
  )

  val socialMessages = socialFeed
    .throttle(1, 2 seconds)
    .map(socialPost => TextMessage(s"${socialPost.owner} said: ${socialPost.content}"))

  val socialFlow: Flow[Message, Message, Any] = Flow.fromSinkAndSource(
    Sink.foreach[Message](println),
    socialMessages
  )

}
