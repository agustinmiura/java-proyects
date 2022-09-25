package part2EvenSourcing

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.PersistentActor

import java.time.LocalDateTime

object PersistentActors extends App {

  case object Shutdown

  case class InvoiceRecorded(id: Int, recipient: String, date: LocalDateTime, amount: Int)
  case class Invoice(recipient: String, date: LocalDateTime, amount: Int)
  case class InvoiceBulk(invoices: List[Invoice])

  class Accountant extends PersistentActor with ActorLogging {

    var latestInvoiceId = 0
    var totalAmount = 0

    override def persistenceId: String = "simple-accountant"

    override def receiveCommand: Receive = {
      case Invoice(recipient, date, amount) =>
        log.info(s"Received invoice for amount $amount")
        val event = InvoiceRecorded(latestInvoiceId, recipient, date, amount)
        persist(InvoiceRecorded(latestInvoiceId, recipient, date, amount)) { event =>
          log.info(s"Recovered from $latestInvoiceId, $recipient, $date , $amount ")
          latestInvoiceId += 1
          totalAmount += amount
          sender() ! "Persistence ack"
          log.info(s"Persisted $event as invoice ${event.id}, total amount : ${totalAmount}")
        }
      case Shutdown =>
          context.stop(self)
      case "print" =>
        log.info(s" $latestInvoiceId, $totalAmount ")
      case InvoiceBulk(invoices) =>
        val invoiceIds = latestInvoiceId to (latestInvoiceId + invoices.size)
        val events = invoices.zip(invoiceIds).map { pair =>
          val id = pair._2
          val invoice = pair._1
          InvoiceRecorded(id, invoice.recipient, invoice.date, invoice.amount)
        }
        persistAll(events) { e =>
          latestInvoiceId += 1
          totalAmount += e.amount
        }
    }

    override def receiveRecover: Receive = {
      case InvoiceRecorded(id, _, _, amount) =>
        latestInvoiceId = id
        totalAmount += amount
    }

    override def onPersistFailure(cause: Throwable, event: Any, seqNr: Long): Unit = {
      log.error(s"Fail to persist event $event, becaose of $cause")
      super.onPersistFailure(cause, event, seqNr)
    }

    override def onPersistRejected(cause: Throwable, event: Any, seqNr: Long): Unit = {
      log.error(s"Persist rejected for $event, because of $cause")
      super.onPersistRejected(cause, event, seqNr)
    }

  }



  val system = ActorSystem("PersistentActors")
  val accountant = system.actorOf(Props[Accountant], "simpleAccountant")

  /*
  for (i <- 1 to 10) {
    accountant ! Invoice("The sofa Company", LocalDateTime.now(), i * 1000)
  }
  */
  val newInvoices = for (i <-1 to 5) yield Invoice("The awesoe chair", LocalDateTime.now(), i * 1000)
  accountant ! InvoiceBulk(newInvoices.toList)
  accountant ! Shutdown



}
