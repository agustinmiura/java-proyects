package part1recap

object ThreadModelLimitations extends App {
  class BankAccount(@volatile private var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int) = this.synchronized {
      amount -= money
    }

    def deposit(money: Int) = this.synchronized {
      amount += money
    }

    def getAmount = amount
  }

  val anAccount = new BankAccount(2000)
  for (_ <- 1 to 1000) {
    new Thread(() => anAccount.withdraw(1)).start()
  }
  for (_ <- 1 to 1000) {
    new Thread(() => anAccount.deposit(1)).start()
  }
  println(anAccount.getAmount)

  var task: Runnable = null
  val runningThread: Thread = new Thread(() => {
    while (true) {
      while (task == null) {
        runningThread.synchronized {
          println("[background] waiting for a task...")
          runningThread.wait()
        }
      }
      task.synchronized {
        println(" I have a task !!!")
        task.run()
        task = null
      }
    }
  })

  def delegateToBackgroundThread(r: Runnable) = {
    if (task == null) task = r
    runningThread.synchronized {
      runningThread.notify()
    }
  }

  runningThread.start()
  Thread.sleep(500)
  delegateToBackgroundThread(() => println(99))
  Thread.sleep(500)
  delegateToBackgroundThread(() => println("Finished"))
}
