package part4

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.util.Timeout

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object IntegratingWithExternalServices extends App {

  implicit val system = ActorSystem("IntegratingWithExternalServices")
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatchers.lookup("dedicated-dispatcher")

  def genericExternalSerivice[A,B](element: A): Future[B] = ???
  case class PagerEvent(application: String, description: String, date: LocalDateTime)

  val eventSource = Source(List(
    PagerEvent("AkkaInfra", "infra broke", LocalDateTime.now()),
    PagerEvent("Fastpipeline", "illegalelemnt", LocalDateTime.now()),
    PagerEvent("AkkaInfra", "Service stopped", LocalDateTime.now()),
    PagerEvent("AkkaInfra", "button won't work", LocalDateTime.now())
  ))

  object PagerService {
    private val engineers = List("Daniel", "John", "Lady Gaga")
    private val emails = Map(
      "Daniel" -> "daniel@rockthejvm.com",
      "John" -> "john@rockthejvm.com",
      "Lady Gaga" -> "ladygaga@rtjvm.com"
    )

    val random = new Random()
    def processEvent(pagerEvent: PagerEvent) = Future {
      println("Processing event")
      val engineerIndex = random.nextInt(5)%3
      val engineer = engineers(engineerIndex.toInt)
      val engineerEmail = emails(engineer)

      // page the engineer
      println(s"Sending engineer $engineerEmail a high priority notification: $pagerEvent")
      Thread.sleep(1000)

      // return the email that was paged
      engineerEmail
    }
  }

  val infraEvents = eventSource.filter(_.application == "AkkaInfra")
  println(infraEvents)
  val pagedEngineerEmails = infraEvents.mapAsync(parallelism = 1)(event => PagerService.processEvent(event))
  // guarantees the relative order of elements
  val pagedEmailsSink = Sink.foreach[String](email => println(s"Successfully sent notification to $email"))
  //pagedEngineerEmails.to(pagedEmailsSink).run()

  class PagerActor extends Actor with ActorLogging {

    override def receive: Receive = {
      case pagerEvent: PagerEvent =>
        sender() ! processEvent(pagerEvent)
    }

    private val engineers = List("Daniel", "John", "Lady Gaga")
    private val emails = Map(
      "Daniel" -> "daniel@rockthejvm.com",
      "John" -> "john@rockthejvm.com",
      "Lady Gaga" -> "ladygaga@rtjvm.com"
    )

    val random = new Random()

    private def processEvent(pagerEvent: PagerEvent) = {
      log.info("Processing event")
      val engineerIndex = random.nextInt(5) % 3
      val engineer = engineers(engineerIndex.toInt)
      val engineerEmail = emails(engineer)

      // page the engineer
      println(s"Sending engineer $engineerEmail a high priority notification: $pagerEvent")
      Thread.sleep(1000)

      // return the email that was paged
      engineerEmail
    }
  }

  import akka.pattern.ask
  import scala.concurrent.duration._

  implicit val timeout = Timeout(3 seconds)
  val pagerActor = system.actorOf(Props[PagerActor], "pagerActor")
  val alternativePagedEngineerEmails = infraEvents.mapAsync(parallelism = 4)(event => (pagerActor ? event).mapTo[String])
  alternativePagedEngineerEmails.to(pagedEmailsSink).run()

}
