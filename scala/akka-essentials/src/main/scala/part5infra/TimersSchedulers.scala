package part5infra

import akka.actor.{Actor, ActorLogging, ActorSystem, Cancellable, Props, Timers}

import scala.concurrent.duration._

object TimersSchedulers extends App {

  class SimpleActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }

  val system = ActorSystem("SchedulersDemo")
  val simpleActor = system.actorOf(Props[SimpleActor])

  system.log.info("Scheduling reminder for a simple actor")

  implicit val executionContext = system.dispatcher
  system.scheduler.scheduleOnce(1 second) {
    simpleActor ! "reminder"
  }

  val routine: Cancellable = system.scheduler.schedule(1 second, 2 second) {
    simpleActor ! "heartbeat"
  }

  /*
  system.scheduler.scheduleOnce(5 seconds) {
    routine.cancel()
  }
  */

  class SelfClosingActor extends Actor with ActorLogging {
    var schedule = createTimeoutWindow()

    def createTimeoutWindow(): Cancellable = {
      context.system.scheduler.scheduleOnce(1 second) {
        self ! "Timeout"
      }
    }

    override def receive: Receive = {
      case "timeout" =>
        log.info("Stopping myself")
        context.stop(self)
      case message =>
        log.info(s"Receive $message, staying alive")
        schedule.cancel()
        schedule = createTimeoutWindow()
    }
  }

  val selfClosingActor = system.actorOf(Props[SelfClosingActor], "selfClosingActor")
  system.scheduler.scheduleOnce(250 millis) {
    selfClosingActor ! "ping"
  }

  /*
  system.scheduler.scheduleOnce(2 seconds) {
    system.log.info("sending poing to the self-closing actor")
    selfClosingActor ! "pong"
  }
  */

  case object TimerKey
  case object Start
  case object Reminder
  case object Stop
  class TimerBasedHeartBeatKey extends Actor with ActorLogging with Timers {
    timers.startSingleTimer(TimerKey, Start, 500 millis)

    override def receive: Receive = {
      case Start =>
        log.info("Bootstraping")
        timers.startPeriodicTimer(TimerKey, Reminder, 1 second)
      case Reminder =>
        log.info("I am alive")
      case Stop =>
        log.warning("Stopping!")
        timers.cancel(TimerKey)
        context.stop(self)
    }
  }

  val timerHeartBeatActor = system.actorOf(Props[TimerBasedHeartBeatKey], "timerActor")
  system.scheduler.scheduleOnce(5 seconds) {
    timerHeartBeatActor ! Stop
  }

}
