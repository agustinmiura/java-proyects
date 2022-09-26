package part2_primer

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}

object Backpressure extends App {

  implicit val system = ActorSystem("Backpressure")
  implicit val materializer = ActorMaterializer()

  val fastSource = Source(1 to 1000)
  val slowSink = Sink.foreach[Int] { x =>
    Thread.sleep(1000)
    println(s"Sink : $x")
  }

  //fastSource.to(slowSink).run()
  //fastSource.async.to(slowSink).run()

  val simpleFlow = Flow[Int].map { x =>
    println(s"Incoming $x")
    x + 1
  }

  /*
  fastSource.async
    .via(simpleFlow).async
    .to(slowSink)
    .run()
  */

  val bufferedFlow = simpleFlow.buffer(10, overflowStrategy = OverflowStrategy.dropTail)
  fastSource.async
    .via(bufferedFlow).async
    .to(slowSink)
  // .run()

  import scala.concurrent.duration._
  fastSource.throttle(2, 1 second).runWith(Sink.foreach(println))

}
