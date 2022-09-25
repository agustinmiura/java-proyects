package part4

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.PersistentActor
import akka.persistence.journal.{EventAdapter, EventSeq}
import com.typesafe.config.ConfigFactory
import part4.DataModel.{WrittenCouponApplied, WrittenCouponAppliedV2}
import part4.DomainModel.{ApplyCoupon, Coupon, CouponApplied, User}

import scala.collection.mutable

object DomainModel {
  case class User(id: String, email: String, name: String)

  case class Coupon(code: String, promotionAmount: Int)

  case class ApplyCoupon(coupon: Coupon, user: User)

  case class CouponApplied(code: String, user: User)
}

class ModelAdapter extends EventAdapter {

  import DomainModel._

  override def manifest(event: Any): String = "CMA"

  override def fromJournal(event: Any, manifest: String): EventSeq = event match {
    case event@WrittenCouponApplied(code, userId, userEmail) =>
      println(s"Converting $event to DOMAIN MODEL ")
      EventSeq.single(CouponApplied(code, User(userId, userEmail, "")))
    case event@WrittenCouponAppliedV2(code, userId, userEmail, username) =>
      println(s"Converting $event to DOMAIN MODEL ")
      EventSeq.single(CouponApplied(code, User(userId, userEmail, username)))
    case other =>
      EventSeq.single(other)
  }

  override def toJournal(event: Any): Any = event match {
    case CouponApplied(code, user) =>
      println(s"Converting $event to DATA model")
      WrittenCouponAppliedV2(code, user.id, user.email, user.name)
  }
}

object DataModel {
  case class WrittenCouponApplied(code: String, userId: String, userEmail: String)
  case class WrittenCouponAppliedV2(code: String, userId: String, userEmail: String, username: String)
}

class CouponManager extends PersistentActor with ActorLogging {

  val coupons: mutable.Map[String, User] = new mutable.HashMap[String, User]()

  override def persistenceId: String = "coupon-manager"

  override def receiveCommand: Receive = {
    case ApplyCoupon(coupon, user) =>
      if (!coupons.contains(coupon.code)) {
        persist(CouponApplied(coupon.code, user)) { e =>
          log.info(s"Persisted $e")
          coupons.put(coupon.code, user)
        }
      }
  }

  override def receiveRecover: Receive = {
    case event@CouponApplied(code, user) =>
      log.info(s"Recovered $event ")
      coupons.put(code, user)
  }

}

object DetachingModels extends App {

  val system = ActorSystem("DetachingModels", ConfigFactory.load().getConfig("detachingModels"))
  val couponManager = system.actorOf(Props[CouponManager], "couponManager")

  val otherCoupons = for (i <- 10 to 15) {
    val coupon = Coupon(s"MEGA_COUPON_$i", 100)
    val user = User(s"$i", s"user$i@gmail.com", s"name$i")
    couponManager ! ApplyCoupon(coupon, user)
  }

}


