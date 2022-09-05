package com.rockthejvm.part3fp

import scala.util.{Failure, Random, Success, Try}

object HandlingFailure extends App {

  val aSuccess = Success(3)
  val aFailure = Failure(new RuntimeException("SUPER FAILURE"))

  println(aSuccess)
  println(aFailure)

  def unsafeMethod(): String = throw new RuntimeException("Failure in method")

  val potentialFailure = Try(unsafeMethod())
  println(potentialFailure)

  val anotherPotentialFailure = Try {

  }

  println(potentialFailure.isSuccess)
  println(potentialFailure.isFailure)

  def backupMethod():String = " A valid result "
  val fallbackTry = Try(unsafeMethod()).orElse(Try(backupMethod()))
  println(fallbackTry)

  def betterUnsafeMethod(): Try[String] = Failure(new RuntimeException)
  def backupMethod2(): Try[String] = Success("A valid result")
  val betterFallback = betterUnsafeMethod() orElse betterUnsafeMethod()
  println(betterFallback)

  println(aSuccess.map(_ * 2))
  println(aSuccess.flatMap(x => Success(x * 10)))
  println(aSuccess.filter(_ > 10))

  val hostName = "localhost"
  val port = "8080"
  def renderHtml(page:String) = println(page)

  class Connection {
    def get(url: String): String = {
      val random = new Random(System.nanoTime())
      if (random.nextBoolean()) "<html>...<html>"
      else throw new RuntimeException("Connection is flaky")
    }
    def getSafe(url:String): Try[String] = {
      return Try(get(url))
    }
  }

  object HttpService {
    val random = new Random(System.nanoTime())
    def getConnection(host:String, port:String): Connection = {
      if (random.nextBoolean()) new Connection
      else throw new RuntimeException("Someone else took the port")
    }
    def getSafeConnection(host:String, port:String): Try[Connection] = {
      Try(getConnection(host, port))
    }
  }

  val possibleConnection = HttpService.getSafeConnection(hostName, port)
  val possibleHtml = possibleConnection.flatMap(connection => connection.getSafe("/home"))
  possibleHtml.foreach(renderHtml)

  HttpService.getSafeConnection(hostName, port)
    .flatMap(connection => connection.getSafe("/home"))
    .foreach(renderHtml)

  for {
    connection <- HttpService.getSafeConnection(hostName, port)
    htmlPage <- connection.getSafe("/home")
  } renderHtml(htmlPage)



}
