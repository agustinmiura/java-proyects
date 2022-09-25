package part3_graph

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, BidiShape, ClosedShape}
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}

object BidirectionalFlows extends App {
  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()

  def encrypt(n: Int)(string: String) = string.map(character => (character+n).toChar)
  def decrypt(n: Int)(string: String) = string.map(character => (character-n).toChar)

  val bidiCryptoStaticGrap = GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val encriptionFlowShape = builder.add(Flow[String].map(encrypt(3)))
    val decriptionFlowShape = builder.add(Flow[String].map(decrypt(3)))

    BidiShape.fromFlows(encriptionFlowShape, decriptionFlowShape)
  }

  val unencryptedStrings = List("akka", "is", "awesome", "testing", "flows")
  val unencrptedSource = Source(unencryptedStrings)
  val encryptedSource = Source(unencryptedStrings.map(encrypt(3)))

  val cryptoBidiGrap = RunnableGraph.fromGraph(
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._
      val unecriptedSource = builder.add(unencrptedSource)
      val bidi = builder.add(bidiCryptoStaticGrap)
      val encryptedSink = Sink.foreach[String](string => println(s"Encrypted : $string"))
      val decrptedSink = builder.add(Sink.foreach[String](string => println(s"Decryptred: $string")))
      unecriptedSource ~> bidi.in1   ; bidi.out1 ~> encryptedSink
      decrptedSink     <~ bidi.out2  ; bidi.in2  <~ encryptedSource
      ClosedShape
    }
  )

  cryptoBidiGrap.run()


}
