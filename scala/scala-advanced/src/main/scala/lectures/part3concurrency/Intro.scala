package lectures.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {

  val aThread = new Thread(() => {
    println("Hello")
  })
  aThread.start()
  aThread.join()

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("Hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("Goodbye")))
  threadHello.start()
  threadGoodbye.start()

  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("Something executable "))
  pool.execute(() => {
    Thread.sleep(1000)
    println("Done after 1 seconds")
  })
  pool.execute(() => {
    Thread.sleep(2000)
    println("Done after 2 seconds")
  })
  pool.execute(() => {
    Thread.sleep(1000)
    println("Done after 3 seconds")
    Thread.sleep(1000)
    println("Done after 4 seconds")
  })
  pool.shutdown()
  println(pool.isShutdown)
  //pool.execute(() => println(" Should not appear "))

}
