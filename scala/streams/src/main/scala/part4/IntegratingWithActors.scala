package part4


import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.Timeout

import scala.concurrent.duration._

object IntegratingWithActors extends App {

  implicit val  system = ActorSystem("IntegratingWithActors")
  implicit val materializer = ActorMaterializer()

  class SimpleActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case s: String =>
          log.info(s"With string $s")
          sender ! s"s$s$s"
      case n:Int =>
        log.info(s"Recevied number : $n")
        sender ! (2*n)
      case _ =>
    }
  }

  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")
  val numbersSource = Source(1 to 10)

  implicit val timeout = Timeout(2 seconds)
  //val actorBasedFlow = Flow[Int].ask[Int](parallelism = 4)(simpleActor)

  //numbersSource.ask[Int](parallelism = 4)(simpleActor).to(Sink.ignore).run()

  val actorPoweredSource = Source.actorRef[Int](bufferSize = 10, overflowStrategy = OverflowStrategy.dropHead)
  val materializedActorRef = actorPoweredSource.to(Sink.foreach[Int](number => println(s"Actor powered flow got number: $number"))).run()
  materializedActorRef ! 10
  materializedActorRef ! akka.actor.Status.Success("complete")

  case object StreamInit
  case object StreamAck
  case object StreamComplete
  case class StreamFail(ex:Throwable)

  class DestinationActor extends Actor with ActorLogging {

    override def receive: Receive = {
      case StreamInit =>
        log.info("Stream initialized")
        sender() ! StreamAck
      case StreamComplete =>
        log.info("Stream completed")
        context.stop(self)
      case StreamFail(ex) =>
        log.info(s"Stream failed: $ex")
      case message =>
        log.info(s"Message : $message , final resting point ")
        sender() ! StreamAck
    }
  }

  val destinationActor = system.actorOf(Props[DestinationActor], "destinationActor")
  val actorPoweredSink = Sink.actorRefWithAck[Int](
    destinationActor,
    onInitMessage = StreamInit,
    onCompleteMessage = StreamComplete,
    ackMessage = StreamAck,
    onFailureMessage = throwable => StreamFail(throwable)
  )

  Source(1 to 10).to(actorPoweredSink).run()

}
