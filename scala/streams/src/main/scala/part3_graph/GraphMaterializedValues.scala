package part3_graph

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, SinkShape}

object GraphMaterializedValues extends App {

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()

  val source = Source(List(
    "Akka",
    "is",
    "awesome",
    "Java",
    "C#"
  ))
  val printer = Sink.foreach[String](println)
  val counter = Sink.fold[Int, String](0)((/**/ count, string) => count + 1)

  val complexWordSink = Sink.fromGraph(
    GraphDSL.create(printer, counter)((printerMatValue, counterMatValue) => counterMatValue) { implicit builder =>
      (printerShape, counterShape) =>
        import GraphDSL.Implicits._
        val broadcast = builder.add(Broadcast[String](2))
        val lowercaseFilter = builder.add(Flow[String].filter(word => word == word.toLowerCase))
        val shortStringFilter = builder.add(Flow[String].filter(string => string.length < 5))

        broadcast ~> lowercaseFilter ~> printerShape
        broadcast ~> shortStringFilter ~> counterShape

        SinkShape(broadcast.in)
    }
  )

  import system.dispatcher

  val future = source.toMat(complexWordSink)(Keep.right).run()
  future.onComplete({ tryElement =>
    if (tryElement.isSuccess) {
      println(s"Sucess count : ${tryElement.get}")
    } else {
      println("Failed")
    }
  })


}
