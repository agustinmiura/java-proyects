package part4

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Sink, Source}

import java.time.LocalDateTime

object AdvancedBackPressure extends App {

  implicit val system = ActorSystem("backpressure")
  implicit val materializer = ActorMaterializer()

  val controlledFlow = Flow[Int].map(_ * 2).buffer(10, OverflowStrategy.dropHead)

  case class PagerEvent(description: String, date:LocalDateTime, nInstances: Int = 1);
  case class Notification(email: String, pagerEvent: PagerEvent)

  val events = List(
    PagerEvent("Service discovery failed", LocalDateTime.now()),
    PagerEvent("Service discovery in the data pipeline", LocalDateTime.now()),
    PagerEvent("Number of 500 http", LocalDateTime.now()),
    PagerEvent("Number of 500 http", LocalDateTime.now())
  )

  val eventSource = Source(events)

  val onCallEngineer = "daniel@rockthejvm.com"

  def sendEmail(notification:Notification) =
    println(s" Dear : ${notification.email} , you have an event : ${notification.pagerEvent} ")

  val notificationSink = Flow[PagerEvent].map(event => Notification(onCallEngineer, event))
    .to(Sink.foreach[Notification](sendEmail))

  //eventSource.to(notificationSink).run()

  def sendEmailSlow(notification: Notification) = {
    Thread.sleep(1000)
    println(s" Dear : ${notification.email} , you have an event : ${notification.pagerEvent} ")
  }

  val aggregateNoticationFlow = Flow[PagerEvent]
    .conflate((event1, event2) => {
      val nInstances = event1.nInstances + event2.nInstances
      PagerEvent(s" You have $nInstances that require", LocalDateTime.now(), nInstances)
    })
    .map(resultingEvent => Notification(onCallEngineer, resultingEvent))

  eventSource.via(aggregateNoticationFlow).async.to(Sink.foreach[Notification](sendEmailSlow)).run()
}
