package com.rockthejvm.part3fp

import scala.util.Random

object Options extends App {

  val myFirstOption: Option[Int] = Some(4)
  val noValueOption: Option[Int] = None

  println(myFirstOption)
  println(noValueOption)

  def unsafeMethod(): String = null
  val result = Option(unsafeMethod())

  println(result)
  def backupMethod(): String = "A valid result"

  val chainedResult = Option(unsafeMethod()).orElse(Option(backupMethod()))
  println(chainedResult)

  def betterUnsafeMethod(): Option[String] = None
  def betterBackupMethod(): Option[String] = Some("A valid result")

  val betterChainedResult = betterUnsafeMethod() orElse betterUnsafeMethod()
  println(betterChainedResult)

  println(myFirstOption.isEmpty)
  println(myFirstOption.get)

  println(myFirstOption.map(_ * 2))
  println(myFirstOption.filter(_ > 10))
  println(myFirstOption.flatMap( x => Option(x * 10) ))

  val config: Map[String, String] = Map(
    "host" -> "176.1.1.1",
    "port" -> "80"
  )

  class Connection {
    def connect = "Connected"
  }

  object Connection {
    val random = new Random(System.nanoTime())
    def apply(host:String, port:String): Option[Connection] = {
      if (random.nextBoolean()) Some(new Connection)
      else None
    }
  }

  val host = config.get("host")
  val port = config.get("port")
  val connection = host.flatMap(h => port.flatMap(p => Connection.apply(h, p)))
  val conectionStatus = connection.map(c => c.connect)
  println(conectionStatus)
  conectionStatus.foreach(println)

  config.get("host")
    .flatMap(host => config.get("port")
    .flatMap(port => Connection(host, port))
    .map(connection => connection.connect))
  .foreach(println)

  val forConnectionStatus = for {
    host <- config.get("host")
    port <- config.get("port")
    connection <- Connection(host, port)
  } yield connection.connect
  forConnectionStatus.foreach(println)

}
