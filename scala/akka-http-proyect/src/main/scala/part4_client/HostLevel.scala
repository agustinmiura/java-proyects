package part4_client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import part4_client.PaymentSystemDomain.PaymentRequest
import spray.json._

import java.util.UUID
import scala.util.{Failure, Success}

object HostLevel extends App with PaymentJsonProtocol {

  implicit val system = ActorSystem("ConnectionLevel")
  implicit val materializer = ActorMaterializer()

  val poolFlow = Http().cachedHostConnectionPool[Int]("www.google.com")

  Source(1 to 10)
    .map(i => (HttpRequest(), i))
    .via(poolFlow)
    .map {
      case (Success(response), value) =>
        response.discardEntityBytes()
        s"Request $value has received response $response"
      case (Failure(ex), value) =>
        s"Request $value has failed $ex"
    }
    .runWith(Sink.foreach[String](println))

  val creditCards = List(
    CreditCard("4242-4242-4242-4242", "424", "tx-test-account"),
    CreditCard("1234-1234-1234-1234", "123", "tx-daniels-account"),
    CreditCard("1234-1234-4321-4321", "321", "my-awesome-account")
  )

  val paymentRequests = creditCards.map(creditCard => PaymentRequest(creditCard, "rtjvm-store-account", 99))
  val serverHttpRequests = paymentRequests.map(paymentRequest =>
    (HttpRequest(
      HttpMethods.POST,
      uri = Uri("/api/payments"),
      entity = HttpEntity(
        ContentTypes.`application/json`,
        paymentRequest.toJson.prettyPrint
      )
    ),
      UUID.randomUUID().toString
    )
  )

  Source(serverHttpRequests)
    .via(Http().cachedHostConnectionPool[String]("localhost", 8080))
    .runForeach {
      case (Success(response), orderId) => println(s"The order id is $orderId was successful and return the response $response")
      case (Failure(ex), orderId) => println(s"Failure with $orderId the order id $ex")
    }

}
