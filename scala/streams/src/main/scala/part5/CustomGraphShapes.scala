package part5

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Balance, GraphDSL, Merge, RunnableGraph, Sink, Source}
import akka.stream._

import scala.collection.immutable
import scala.concurrent.duration.DurationInt

object CustomGraphShapes extends App {

  implicit val system = ActorSystem("CustomGraphShape")
  implicit val materializer = ActorMaterializer()

  case class Balance2x3(
                         in0: Inlet[Int],
                         in1: Inlet[Int],
                         out0: Outlet[Int],
                         out1: Outlet[Int],
                         out2: Outlet[Int]
                       ) extends Shape {

    override val inlets: immutable.Seq[Inlet[_]] = List(in0, in1)
    override val outlets: immutable.Seq[Outlet[_]] = List(out0, out1, out2)

    override def deepCopy(): Shape = Balance2x3(
      in0.carbonCopy(),
      in1.carbonCopy(),
      out0.carbonCopy(),
      out1.carbonCopy(),
      out2.carbonCopy()
    )
  }

  val balanceImpl = GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val merge = builder.add(Merge[Int](2))
    val balance = builder.add(Balance[Int](3))

    merge ~> balance

    Balance2x3(
      merge.in(0),
      merge.in(1),
      balance.out(0),
      balance.out(1),
      balance.out(2)
    )
  }

  val balanceGraph = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val slowSource = Source(Stream.from(1)).throttle(1, 1 second)
      val fastSource = Source(Stream.from(1)).throttle(1, 1 second)

      def createSink(index: Int) = Sink.fold(0)((count: Int, element: Int) => {
        println(s"[sink $index] Received $element, current count is $count")
        count + 1
      })

      val sink1 = builder.add(createSink(1))
      val sink2 = builder.add(createSink(2))
      val sink3 = builder.add(createSink(3))

      val balance2x3 = builder.add(balanceImpl)


      slowSource ~> balance2x3.in0
      fastSource ~> balance2x3.in1

      balance2x3.out0 ~> sink1
      balance2x3.out1 ~> sink2
      balance2x3.out2 ~> sink3

      ClosedShape
    }
  )

  //balanceGraph.run()

  case class BalanceMxN[T](override val inlets: List[Inlet[T]], override val outlets: List[Outlet[T]]) extends Shape {
    override def deepCopy(): Shape = BalanceMxN(inlets.map(_.carbonCopy()), outlets.map(_.carbonCopy()))
  }

  object BalanceMxN {
    def apply[T](inputCount: Int, outputCount: Int): Graph[BalanceMxN[T], NotUsed] =
      GraphDSL.create() { implicit builder =>
        import GraphDSL.Implicits._

        val merge = builder.add(Merge[T](inputCount))
        val balance = builder.add(Balance[T](outputCount))

        merge ~> balance

        BalanceMxN(merge.inlets.toList, balance.outlets.toList)
      }
  }

  val balanceMxNGraph = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val slowSource = Source(Stream.from(1)).throttle(1, 1 second)
      val fastSource = Source(Stream.from(1)).throttle(2, 1 second)

      def createSink(index: Int) = Sink.fold(0)((count: Int, element: Int) => {
        println(s"[sink $index] Received $element, current count is $count")
        count + 1
      })

      val sink1 = builder.add(createSink(1))
      val sink2 = builder.add(createSink(2))
      val sink3 = builder.add(createSink(3))

      val balance2x3 = builder.add(BalanceMxN[Int](2, 3))

      slowSource ~> balance2x3.inlets(0)
      fastSource ~> balance2x3.inlets(1)

      balance2x3.outlets(0) ~> sink1
      balance2x3.outlets(1) ~> sink2
      balance2x3.outlets(2) ~> sink3

      ClosedShape
    }
  )

  balanceMxNGraph.run()


}
