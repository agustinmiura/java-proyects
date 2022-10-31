package part3_highlevelserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtSprayJson}
import spray.json._

import java.util.concurrent.TimeUnit
import scala.util.{Failure, Success}

object SecurityDomain extends DefaultJsonProtocol {
  case class LoginRequest(username: String, password: String)

  implicit val loginRequestFormat = jsonFormat2(LoginRequest)
}

object JwtAuthorization extends App with SprayJsonSupport {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import SecurityDomain._

  val algorithm = JwtAlgorithm.HS256
  val secretKey = "rockthejvmsecret"

  val superSecretPasswordDb = Map(
    "admin" -> "admin",
    "daniel" -> "daniel"
  )

  def checkPassword(username: String, password: String): Boolean =
    superSecretPasswordDb.contains(username) && superSecretPasswordDb(username) == password

  def createToken(username: String, expirationPeriodInDays: Int): String = {
    val claims = JwtClaim(
      expiration = Some(System.currentTimeMillis() / 1000 + TimeUnit.DAYS.toSeconds(expirationPeriodInDays)),
      issuedAt = Some(System.currentTimeMillis() / 1000),
      issuer = Some("website.com")
    )


    JwtSprayJson.encode(claims, secretKey, algorithm) // JWT string
  }

  def isTokenExpired(token: String): Boolean = JwtSprayJson.decode(token, secretKey, Seq(algorithm)) match {
    case Success(claims) => claims.expiration.getOrElse(0L) < System.currentTimeMillis() / 1000
    case Failure(_) => true
  }

  def isTokenValid(token: String): Boolean = JwtSprayJson.isValid(token, secretKey, Seq(algorithm))

  val loginRoute =
    post {
      entity(as[LoginRequest]) {
        case LoginRequest(username, password) => if (checkPassword(username, password)) {
          val token = createToken(username, 1)
          respondWithHeader(RawHeader("Access-Token", token)) {
            complete(StatusCodes.OK)
          }
        } else {
          complete(StatusCodes.Forbidden)
        }
        case _ => complete(StatusCodes.Unauthorized)
      }
    }

  val authenticatedRoute =
    (path("secureEndpoint") & get) {
      optionalHeaderValueByName("Authorization") {
        case Some(token) =>
          if (isTokenValid(token)) {
            if (isTokenExpired(token)) {
              complete(HttpResponse(StatusCodes.Unauthorized))
            } else {
              complete("User accessed authorized endpoint!")
            }
          } else {
            complete(HttpResponse(StatusCodes.Unauthorized))
          }
        case _ => complete(HttpResponse(StatusCodes.Unauthorized))
      }
    }

  val route = loginRoute ~ authenticatedRoute

  Http().bindAndHandle(route, "127.0.0.1", 10000)
}
