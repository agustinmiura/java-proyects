package part4

import akka.actor.ActorSystem
import akka.stream.{ActorAttributes, ActorMaterializer}
import akka.stream.scaladsl.{RestartSource, Sink, Source}

import scala.concurrent.duration.DurationInt
import scala.util.Random

object FaultTolerance extends App {

  implicit val system = ActorSystem("FaultTolerance")
  implicit val materialzer = ActorMaterializer()

  val faultySource = Source(1 to 10).map(e => if (e == 6) throw new RuntimeException("Error") else e)
  faultySource.log("tracingElements").to(Sink.ignore).run()

  faultySource.recover {
    case _: RuntimeException => Int.MinValue
  }.log("gracefulSource")
    .to(Sink.ignore)
    .run()

  faultySource.recoverWithRetries(3, {
    case _: RuntimeException => Source(90 to 99)
  })
    .log("recoverWithRetries")
    .to(Sink.ignore)
  // .run()

  val randomGenerator = new Random()
  val restartSource = RestartSource.onFailuresWithBackoff(
    minBackoff = 1 second,
    maxBackoff = 30 seconds,
    randomFactor = 0.2
  )(() => {
    val randomNumber = randomGenerator.nextInt(20)
    Source(1 to 10).map(elem => if (elem == randomNumber) throw new RuntimeException else elem)
  })
  restartSource
    .log("R estartbackooff")
    .to(Sink.ignore)
    .run()


}
