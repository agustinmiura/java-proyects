package part2_primer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Future

object FirstPrinciples extends App {

  implicit val system = ActorSystem("FirstPrinciples")
  implicit val materializer = ActorMaterializer()

  val source = Source(1 to 10)
  val sink = Sink.foreach[Int](println)

  val graph = source.to(sink)
  graph.run()

  val flow = Flow[Int].map(x => x + 1)
  val sourceWithFlow = source.via(flow)
  val flowWithSink = flow.to(sink)

  /*
  sourceWithFlow.to(sink).run()
  source.to(flowWithSink).run()
  source.via(flow).to(sink).run()
  */

  val finiteSource = Source.single(1)
  val anotherFiniteSource = Source(List(1, 2, 3))
  val emptySource = Source.empty[Int]
  val infiniteSource = Source(Stream.from(1))

  import scala.concurrent.ExecutionContext.Implicits.global

  val futureSource = Source.fromFuture(Future(42))

  val simpleSink = Sink.ignore
  val foreachSink = Sink.foreach[String](println)
  val headSink = Sink.head[Int]
  val foldSink = Sink.fold[Int, Int](0)((a, b) => a + b)

  val mapFlow = Flow[Int].map(x => 2 * x)
  val takeFlow = Flow[Int].take(5)

  val doubleFlowGraph = source.via(mapFlow).via(takeFlow).to(sink)
  //doubleFlowGraph.run()

  val mapSource = Source(1 to 10).map(x => x * 2)
  //mapSource.runForeach(println)

  /**
   * Create a stream takes the names of persons thne you will keep the first two names with length
   * > 5 chars
   */
  case class Person(firstName: String, lastName: String);
  val persons = List(Person("first", "first"), Person("second", "second"), Person("one", "onew"))
  val personSource = Source(persons).filter(p => p.firstName.length >= 5).take(2)
  val personSink = Sink.foreach(println)
  val personGraph = personSource.to(personSink)
  personGraph.run()

  val names = List("Alice", "Bob", "Charlie", "David", "Martin", "AkkaStreams")
  val sourceNames = Source(names)
  val longName = Flow[String].filter(name => name.length >= 5)
  val limitFlow = Flow[String].take(2)
  val nameSink = Sink.foreach[String](println)
  sourceNames.via(longName).via(limitFlow).to(nameSink).run()

}
