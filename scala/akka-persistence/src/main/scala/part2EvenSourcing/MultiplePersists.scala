package part2EvenSourcing

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.persistence.PersistentActor

import java.sql.Date
import java.time.LocalDateTime

object MultiplePersists extends App {

  case class Invoice(recipient: String, date: LocalDateTime, amount: Int)

  case class TaxRecord(taxId: String, recordId: Int, date: LocalDateTime, totalAmount: Int)

  case class InvoiceRecord(invoiceRecordId: Int, recipient: String, date: LocalDateTime, amount: Int)

  object DiligentAccountant {
    def props(taxId: String, taxtAuthority: ActorRef) = Props(new DiligentAccountant(taxId, taxtAuthority))
  }

  class DiligentAccountant(taxId: String, taxtAuthority: ActorRef) extends PersistentActor with ActorLogging {

    var latestTaxRecordId = 0
    var latestInvoiceRecordId = 0

    override def persistenceId: String = "diligent-accountant"

    override def receiveCommand: Receive = {
      case Invoice(recipient, date, amount) =>
        persist(TaxRecord(taxId, latestTaxRecordId, date, amount / 3)) { record =>
          taxAuthority ! record
          latestTaxRecordId += 1
          persist("Invoice here") { declaration =>
            taxtAuthority ! declaration
          }
        }
        persist(InvoiceRecord(latestInvoiceRecordId, recipient, date, amount)) { invoiceRecord =>
          taxtAuthority ! invoiceRecord
          latestInvoiceRecordId += 1
          persist("InvoiceRecord here") { declaration =>
            taxtAuthority ! declaration
          }
        }
    }

    override def receiveRecover: Receive = {
      case event => log.info(s"Recovered $event")
    }
  }

  class TaxAuthority extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(s"Receive: $message")
    }
  }

  val system = ActorSystem("MultiplePersistDemo")
  val taxAuthority = system.actorOf(Props[TaxAuthority], "HRMC")
  val accountant = system.actorOf(DiligentAccountant.props("UK_33_11", taxAuthority))

  accountant ! Invoice("The Sofa Company", LocalDateTime.now(), 2000)


}
