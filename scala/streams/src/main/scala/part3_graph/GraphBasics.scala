package part3_graph

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.GraphDSL.Implicits.{SourceArrow, fanOut2flow}
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Balance, Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source, Zip}

object GraphBasics extends App {

  implicit val system = ActorSystem("GraphBasics")
  implicit val materializer = ActorMaterializer()

  val input = Source(1 to 1000)
  val incrementer = Flow[Int].map(x => x + 1)
  val multiplier = Flow[Int].map(x => x * 10)
  val output = Sink.foreach[(Int, Int)](println)

  val graph = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._
      val broadcast = builder.add(Broadcast[Int](2))
      val zip = builder.add(Zip[Int,Int])
      input ~> broadcast

      broadcast.out(0) ~> incrementer ~> zip.in0
      broadcast.out(1) ~> multiplier ~> zip.in1

      zip.out ~> output

      ClosedShape
    }
  )
  //graph.run()

  /**
   * Feed an input into 2 sinks at different time
   */
  val sameFlow1 = Flow[Int].map(x => x)
  val sameFlow2 = Flow[Int].map(x => x)
  val output1 = Sink.foreach[(Int)](println)
  val output2 = Sink.foreach[(Int)](println)

  val doubleFeedGraph = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._
      val broadcast = builder.add(Broadcast[Int](2))
      input ~> broadcast

      broadcast.out(0) ~> sameFlow1 ~> output1
      broadcast.out(1) ~> sameFlow2 ~> output2

      ClosedShape
    }
  )
  //doubleFeedGraph.run()

  val firstSink = Sink.foreach[Int](x => println(s"First sink $x"))
  val secondSink = Sink.foreach[Int](x => println(s"Second sink $x"))

  val sourceToTwoSinksGraph = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val broadcast = builder.add(Broadcast[Int](2))
      input ~> broadcast ~> firstSink
               broadcast ~> secondSink

      ClosedShape
    }
  )
  //sourceToTwoSinksGraph.run()

  /**
   * slow,fast => merge => balance => sink1 , sink2
   */
  import scala.concurrent.duration._
  val fastSource = input.throttle(5, 1 second)
  val slowSource = input.throttle(5, 1 second)

  val sink1 = Sink.foreach[Int](x => println(s"Sink1 : $x"))
  val sink2 = Sink.foreach[Int](x => println(s"Sink2 : $x"))

  val balanceGraph = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder =>

      val merge = builder.add(Merge[Int](2))
      val balance = builder.add(Balance[Int](2))

      fastSource ~> merge ~> balance ~> sink1
      slowSource ~> merge
      balance ~> sink2

      ClosedShape
    }
  )
  balanceGraph.run()

}
