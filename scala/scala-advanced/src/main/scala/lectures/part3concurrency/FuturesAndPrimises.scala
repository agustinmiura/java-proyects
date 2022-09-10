package lectures.part3concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration.*

object FuturesAndPrimises extends App {

  def calculateMeaningOfLine: Int = {
    Thread.sleep(2000)
    42
  }

  val aFutureObject = Future {
    calculateMeaningOfLine
  }

  println(aFutureObject.value)

  println("Waiting for the future")
  aFutureObject.onComplete(t => t match {
    case Success(meaningOfLife) => println(s"The meaning of life is $meaningOfLife")
    case Failure(e) => println(s"I have failed with $e")
  })

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) = {
      println(s"${this.name} poking ${anotherProfile.name}")
    }
  }

  object SocialNetwork {
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )
    val random = new Random()

    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }

  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
  /*
  mark.onComplete {
    case Success(markProfile) => {
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e) => e.printStackTrace()
      }
    }
    case Failure(ex) => ex.printStackTrace()
  }
  */

  val nameOnTheWall = mark.map(profile => profile.name)
  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  val zucksBestFriendnRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover {
    case e: Throwable => Profile("fb.id.0-dummy", "Forever alone")
  }

  val aFetchProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
  }

  val fallbackResult = SocialNetwork.fetchProfile("unkown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

  case class User(name: String)
  case class Transaction(sender:String, receiver: String, amount:Double, status: String)

  val random = Random();
  object BankingApp {
    val name = "Rock the JVM banking"
    def fetchUser(name: String): Future[User] = Future {
      Thread.sleep(500)
      User(name)
    }
    def createTransaction(user:User, merchantName: String, amountDouble: Double): Future[Transaction] = Future {
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amountDouble, "SUCCESS")
    }
    def purchase(username:String, item: String, merchantName: String, cost: Double): String = {
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      Await.result(transactionStatusFuture, 2.seconds)
    }
  }

  println(BankingApp.purchase("Daniel", "iPhone 12", "rock the jvm stgore", 3000))

  val aPromise = Promise[Int]()
  val future = aPromise.future

  future.onComplete {
    case Success(r) => println("[consumer] I've received  the " + r)
  }

  val producer = new Thread(() => {
    println("[producer] crunching numbers ...")
    Thread.sleep(1000)
    aPromise.success(42)
    println("[producer] done")
  })

  producer.start()
  Thread.sleep(1000)

  def fullfillInmediately[T](value: T): Future[T] = Future(value)

  def inSequence[A,B](first: Future[A], second: Future[B]): Future[B] = {
    first.flatMap(_ => second)
  }

  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A]
    def tryComplete(promise: Promise[A], result: Try[A]): Unit = result match {
      case Success(r) => try {
        promise.success(r)
      } catch {
        case _ =>
      }
    }
    fa.onComplete(tryComplete(promise, _))
    fb.onComplete(tryComplete(promise, _))
    promise.future
  }


  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]
    val checkAndComplete = (result: Try[A]) =>
      if (!bothPromise.tryComplete(result))
        lastPromise.complete(result)
    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)
    lastPromise.future
  }

  println("Done!")

}
