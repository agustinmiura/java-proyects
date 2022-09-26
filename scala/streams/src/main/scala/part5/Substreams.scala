package part5

import akka.actor.ActorSystem
import akka.actor.Status.Failure
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}

import scala.util.Success

object Substreams extends App {

  implicit val system = ActorSystem("Substreams")
  implicit val materialized = ActorMaterializer()
  import system.dispatcher

  val wordsSource = Source(List(
    "Akka", "is", "amazing", "learning", "subtreams"
  ))

  val groups = wordsSource.groupBy(30, word => if (word.isEmpty) '\0' else word.toLowerCase().charAt(0))
  groups.to(Sink.fold(0)((count, word) => {
    val newCount = count + 1
    println(s"I have just received $word, count is $newCount")
    newCount
  }))
    .run()

  val textSource = Source(List(
    "I love akka streams",
    "This is amazing",
    "learning from rock the jvm"
  ))

  val totalCharCountFuture = textSource
    .groupBy(2, string => string.length % 2)
    .map(_.length) // do your expensive computation here
    .mergeSubstreams //WithParallelism(2)
    .toMat(Sink.reduce[Int](_ + _))(Keep.right)
    .run()

  totalCharCountFuture.onComplete { tryElement =>
    println(tryElement)
  }

  val simpleSource = Source(1 to 5)
  simpleSource.flatMapConcat(x => Source(x to (3 * x))).runWith(Sink.foreach(println))

}
