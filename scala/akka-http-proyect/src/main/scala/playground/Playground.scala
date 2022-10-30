package playground

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn

object Playground extends App {

  implicit val system = ActorSystem("AkkaHttpPlayground")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

}
