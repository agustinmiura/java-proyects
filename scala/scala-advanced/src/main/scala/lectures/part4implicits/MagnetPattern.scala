package lectures.part4implicits

import scala.concurrent.Future

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

  receive(new P2PRequest)
  receive(new P2PResposes)

}
