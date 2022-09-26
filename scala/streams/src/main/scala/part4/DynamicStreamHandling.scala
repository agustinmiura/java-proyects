package part4

import akka.actor.ActorSystem
import akka.stream.scaladsl.{BroadcastHub, Keep, MergeHub, Sink, Source}
import akka.stream.{ActorMaterializer, KillSwitches}

import scala.concurrent.duration.DurationInt

object DynamicStreamHandling extends App {

  import system.dispatcher

  implicit val system = ActorSystem("DynamicStreamHandling")
  implicit val materializer = ActorMaterializer()

  val killSwitchFlow = KillSwitches.single[Int]
  val counter = Source(Stream.from(1)).throttle(1, 1 second)
  val sink = Sink.ignore

  val killSwitch = counter
    .viaMat(killSwitchFlow)(Keep.right)
    .to(sink)
    .run()

  system.scheduler.scheduleOnce(3 seconds) {
    killSwitch.shutdown()
  }

  val dynamicMerge = MergeHub.source[Int]
  val materializedSink = dynamicMerge.to(Sink.foreach[Int](println)).run()

  Source(1 to 10).runWith(materializedSink)
  counter.runWith(materializedSink)

  val dynamicBroadcast = BroadcastHub.sink[Int]
  val materializedSource = Source(1 to 100).runWith(dynamicBroadcast)
  //materializedSource.runWith(Sink.ignore)
  //materializedSource.runWith(Sink.foreach[Int](println))

  val merge = MergeHub.source[String]
  val bcast = BroadcastHub.sink[String]
  val (publisher, subscriber) = merge.toMat(bcast)(Keep.both).run()

  subscriber.runWith(Sink.foreach(e => println(s" I received $e")))
  subscriber.map(string => string.length).runWith(Sink.foreach(n => println(s"I got $n")))

  Source(List("Akka", "is", "amaing")).runWith(publisher)
  Source(List("I", "love", "scala")).runWith(publisher)

}
