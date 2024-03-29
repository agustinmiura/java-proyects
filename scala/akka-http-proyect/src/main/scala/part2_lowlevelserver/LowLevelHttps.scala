package part2_lowlevelserver

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.{ConnectionContext, Http, HttpConnectionContext, HttpsConnectionContext}
import akka.stream.ActorMaterializer

import java.io.InputStream
import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}


object HttpsContext {
  val ks: KeyStore = KeyStore.getInstance("PKCS12")
  val keyStoreFile: InputStream = getClass.getClassLoader.getResourceAsStream("keystore.pkcs12")
  val password = "akka-https".toCharArray
  ks.load(keyStoreFile, password)

  val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
  keyManagerFactory.init(ks, password)

  val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
  trustManagerFactory.init(ks)

  val sslContext: SSLContext = SSLContext.getInstance("TLS")
  sslContext.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom)

  val httpsConnectionContext: HttpsConnectionContext = ConnectionContext.https(sslContext)
}

object LowLevelHttps extends App {

  implicit val system = ActorSystem("LowLevelHttps")
  implicit val materrializer = ActorMaterializer()

  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(HttpMethods.GET, _, _, _, _) =>
      HttpResponse(
        StatusCodes.OK, // HTTP 200
        entity = HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          """
            |<html>
            | <body>
            |   Hello from Akka HTTP!
            | </body>
            |</html>
            """.stripMargin
        )
      )

    case request: HttpRequest =>
      request.discardEntityBytes()
      HttpResponse(
        StatusCodes.NotFound, // 404
        entity = HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          """
            |<html>
            | <body>
            |   OOPS! The resource can't be found.
            | </body>
            |</html>
            """.stripMargin
        )
      )
  }

  val httpsBinding = Http().bindAndHandleSync(requestHandler, "localhost", 8443, HttpsContext.httpsConnectionContext)

}
