package part2_primer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}

object MaterializingStreams extends App {

  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()

  val simpleGraph = Source(1 to 10).to(Sink.foreach(println))
  val simpleMaterializedValue = simpleGraph.run()

  import system.dispatcher

  val source = Source(1 to 10)
  val sink = Sink.reduce[Int]((a, b) => a + b)
  val sumfuture = source.runWith(sink)
  sumfuture.onComplete { element =>
    println(element)
  }

  val aSource = Source(1 to 10)
  val aFlow = Flow[Int].map(x => x + 1)
  val aSink = Sink.foreach[Int](println)
  val graph = aSource.viaMat(aFlow)(Keep.right).toMat(aSink)(Keep.right)
  graph.run().onComplete { result =>
    println(result.getClass)
  }

  /*
  Source(1 to 10).runWith(Sink.reduce(_ + _))
  Source(1 to 10).reduce(_ + _)
  Sink.foreach[Int](println).runWith(Source.single(42))
  Flow[Int].map(x => 2 * x).runWith(aSource, aSink)
  */

  /**
   * Last element of source
   * Total word count
   */
  val lastGraph = Source(1 to 10).runWith(Sink.last)
  /*
  lastGraph.onComplete { value =>
    println(value)
  }
  */
  val words = List("Akka Akka Akka", "Akka Akka Akka Akka", "Akka Akka Akka Akka")
  val sentenceSource = Source(words)
  val wordSource = Source(words)
  val wordGraph = wordSource.map(string => string.split(" ")).map(wordArray => wordArray.length).fold(0)((x, y) => x + y).reduce((x, y) => x + y) runWith (Sink.last)
  wordGraph.onComplete { value =>
    println(value.toString)
  }

  val f1 = Source(1 to 10).toMat(Sink.last)(Keep.right).run()
  val f2 = Source(1 to 10).runWith(Sink.last)

  val wordCount = Sink.fold[Int, String](0)((currentWords, newSentence) => currentWords + newSentence.split(" ").length)
  val g1 = wordSource.toMat(wordCount)(Keep.right).run()
  val g2 = wordSource.runWith(wordCount)
  val g3 = wordSource.runFold(0)((currentWords, newSentence) => currentWords + newSentence.split(" ").length)

}
