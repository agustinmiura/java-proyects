package part6patterns


import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Cancellable}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, OneInstancePerTest, WordSpecLike}

class FSMSpec extends TestKit(ActorSystem("FSMSpec"))
  with ImplicitSender with WordSpecLike with BeforeAndAfterAll with OneInstancePerTest {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

}

object FSMSpec {

  case class Initialize(inventory: Map[String, Int], prices: Map[String, Int])

  case class RequestProduct(product: String)

  case class Instruction(instruction: String) // message the VM will show on its "screen"

  case class ReceiveMoney(amount: Int)

  case class Deliver(product: String)

  case class GiveBackChange(amount: Int)

  case class VendingError(reason: String)

  case object ReceiveMoneyTimeout

  class VendingMachine extends Actor with ActorLogging {
    override def receive: Receive = idle

    def idle: Receive = {
      case Initialize(inventory, prices) => context.become(operational(inventory, prices))
      case _ => sender() ! VendingError("MachineNotInitialized")
    }

    def operational(inventory: Map[String, Int], prices: Map[String, Int]): Receive = {
      case RequestProduct(product) => inventory.get(product) match {
        case None | Some(0) =>
          sender() ! VendingError("ProductNotAvailable")
        case Some(_) =>
          val price = prices(product)
          sender() ! Instruction(s"Please insert $price dollars")
          context.become(waitForMoney(inventory, prices, product, 0, startReceiveMoneyTimeoutSchedule, sender()))
      }
    }

    def waitForMoney(
                      inventory: Map[String, Int],
                      prices: Map[String, Int],
                      product: String,
                      money: Int,
                      moneyTimeoutSchedule: Cancellable,
                      requester: ActorRef
                    ): Receive = {
      case ReceiveMoneyTimeout =>
        requester ! VendingError("RequestTimedOut")
        if (money > 0) requester ! GiveBackChange(money)
        context.become(operational(inventory, prices))
      case ReceiveMoney(amount) =>
        moneyTimeoutSchedule.cancel()
        val price = prices(product)
        if (money + amount >= price) {
          // user buys product
          requester ! Deliver(product)
          // deliver the change
          if (money + amount - price > 0) requester ! GiveBackChange(money + amount - price)
          // updating inventory
          val newStock = inventory(product) - 1
          val newInventory = inventory + (product -> newStock)
          context.become(operational(newInventory, prices))
        } else {
          val remainingMoney = price - money - amount
          requester ! Instruction(s"Please insert $remainingMoney dollars")
          context.become(waitForMoney(
            inventory, prices, product, // don't change
            money + amount, // user has inserted some money
            startReceiveMoneyTimeoutSchedule, // I need to set the timeout again
            requester))
        }
    }
  }

}