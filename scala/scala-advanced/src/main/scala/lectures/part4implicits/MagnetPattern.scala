package lectures.part4implicits

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {

  class P2PRequest
  class P2PResposes
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResposes): Int
    def receive[T : Serializer](message: T): Int
    def receive[T : Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
  }

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    def apply(): Int = {
      println("Hanlding P2P request")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResposes) extends MessageMagnet[Int] {
    def apply(): Int = {
      println("Hanlding P2P response")
      42
    }
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }

  implicit class FromResponseFuture(future: Future[P2PResposes])  extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }

  receive((Future(new P2PResposes)))
  receive((Future(new P2PRequest)))
  receive(new P2PRequest)
  receive(new P2PResposes)

  trait MathLib {
    def add1(x: Int): Int = x + 1
    def add1(s: String): Int = s.toInt + 1
  }

  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(x: String) extends AddMagnet {
    override  def apply(): Int = x.toInt + 1
  }

  def addFv = add1 _
  println(addFv(1))
  println(addFv("3"))

}
