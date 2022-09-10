package lectures.part3concurrency

object ConcurrencyProblems {

  def runInParallel(): Unit = {
    var x = 0
    val thread1 = new Thread(() => {
      x = 1
    })
    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    Thread.sleep(400)
    println(" The value of x is : " + x)
  }

  case class BankAccount(var amount: Int)

  def buy(bankAcount: BankAccount, thing: String, price: Int): Unit = {
    bankAcount.amount -= price
  }

  def buySafe(bankAcount: BankAccount, thing: String, price: Int): Unit = {
    bankAcount.synchronized {
      bankAcount.amount -= price
    }
  }

  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buySafe(account, "shoes", 3000))
      val thread2 = new Thread(() => buySafe(account, "iPhone", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (account.amount != 46000) println(s" I have just broke the bank : ${account.amount} ")
    }

    def minMaxX(): Unit = {
      var x = 0
      val threads = (1 to 100).map(_ => new Thread(() => {
        x += 1
      }))
      threads.foreach(_.start)
    }

    def demoSleepFallacy(): Unit = {
      var message = ""
      val awesomeThread = new Thread(() => {
        Thread.sleep(1000)
        message = "Scala is awesome"
      })
      message = "Scala sucks"
      awesomeThread.start()
      Thread.sleep(1001)
      println(message)
    }

  }

  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread =
    new Thread(() => {
      if (i < maxThreads) {
        val newThread = inceptionThreads(maxThreads, i + 1)
        newThread.start()
        newThread.join()
      }
      println(s"Helo from thread $i")
    })

}
