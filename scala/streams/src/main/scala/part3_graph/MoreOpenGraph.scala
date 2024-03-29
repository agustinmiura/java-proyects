package part3_graph

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape, FanOutShape2, UniformFanInShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source, ZipWith}

import java.time.LocalDateTime

object  MoreOpenGraph extends App{

  implicit val system = ActorSystem("openGraph")
  implicit val materializer = ActorMaterializer()

  val max3StaticGraph = GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val max1 = builder.add(ZipWith[Int, Int, Int]((a,b)=> Math.max(a,b)))
    val max2 = builder.add(ZipWith[Int, Int, Int]((a,b)=> Math.max(a,b)))

    max1.out ~> max2.in0
    UniformFanInShape(max2.out, max1.in0, max1.in1, max2.in1)
  }

  val source1 = Source(1 to 10)
  val source2 = Source((1 to 10).map(_ => 5))
  val source3 = Source((1 to 10).reverse)

  val maxSink = Sink.foreach[Int](x => println(s"Max is $x"))

  val max3RunnableGraph = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val max3Shape = builder.add(max3StaticGraph)
      source1 ~> max3Shape.in(0)
      source2 ~> max3Shape.in(1)
      source3 ~> max3Shape.in(2)
      max3Shape.out ~> maxSink

      ClosedShape
    }
  )
  //max3RunnableGraph.run()

  case class Transaction(id: String, source: String, recipient: String, amount: Int, date: LocalDateTime)
  val transactionSource = Source(List(
    Transaction("1111", "Paul", "Jim", 100, LocalDateTime.now()),
    Transaction("222", "Daniel", "Jim", 100000, LocalDateTime.now()),
    Transaction("3333", "Jim", "Aline", 70000, LocalDateTime.now())
  ))
  val bankProcessor = Sink.foreach[Transaction](println)
  val suspiciousAnalysisService =Sink.foreach[String](txnId => println(s"suspicous $txnId"))

  val MoneyGraph = GraphDSL.create() {implicit builder =>
    import GraphDSL.Implicits._

    val broadcast = builder.add(Broadcast[Transaction](2))
    val filter = builder.add(Flow[Transaction].filter(txn => txn.amount > 10000))
    val idExtractor = builder.add(Flow[Transaction].map[String](txn => txn.id))

    broadcast.out(0) ~> filter ~> idExtractor

    new FanOutShape2(broadcast.in, broadcast.out(1), idExtractor.out)
  }
  val suspiciousTxnRunnableGraph = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._
      val suspiciousTxnShape = builder.add(MoneyGraph)
      transactionSource ~> suspiciousTxnShape.in
      suspiciousTxnShape.out0 ~> bankProcessor
      suspiciousTxnShape.out1 ~> suspiciousAnalysisService
      ClosedShape
    }
  )
  suspiciousTxnRunnableGraph.run()

}
