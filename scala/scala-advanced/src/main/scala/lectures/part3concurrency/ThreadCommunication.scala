package lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def get: Int = {
      val result = value
      value = 0
      result
    }

    def set(newValue: Int): Unit = value = newValue
  }

  def naiveProdConsummer(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("[consumer] waiting ...")
      while (container.isEmpty) {
        println("[consumer] actively waiting ...")
      }
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing ...")
      Thread.sleep(500)
      val value = 42
      println("[producer] I have produced , after loing work the value : " + value)
      container.set(value)
    })

    consumer.start()
    producer.start()
  }

  def smartProdCons(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      container.synchronized {
        container.wait()
      }
    })
    println("[consumer] I have consumed " + container.get)

    val producer = new Thread(() => {
      println("[producer] Hard at work")
      Thread.sleep(2000)
      val value = 42

      container.synchronized {
        println("[producer] I'm producing " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

  def prodConsLargeBuffer(): Unit = {

    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("s[consumer $id] buffer empty, waiting ...")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println("[consumer] consumed" + x)

          buffer.notify()
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0
      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting ...")
            buffer.wait()
          }

          println("[producer] producing : " + i)
          buffer.enqueue(i)

          buffer.notify()

          i += 1

        }
        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          while (buffer.isEmpty) {
            println(s"[consumer$id] buffer empty, waiting ...")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println(s"[consumer $id] consumed" + x)
          buffer.notify()
        }

        Thread.sleep(random.nextInt(500))
      }
    }
  }

  val capacity = 3

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0
      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer $id] buffer is full, waiting ...")
            buffer.wait()
          }
          println("s[producer $id] producing : " + i)
          buffer.enqueue(i)
          buffer.notify()
          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiProdCons(consumerQty: Int, producersQty: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 10

    (1 to consumerQty).foreach(i => new Consumer(i, buffer).start())
    (1 to producersQty).foreach(i => new Producer(i, buffer, capacity).start())
  }

  def testNotifyAll(): Unit = {
    val bell = new Object
    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized {
        println(s"[thread $i] waiting ...")
        bell.wait()
      }
    }).start())

    new Thread(() => {
      Thread.sleep(2000)
      println("[announcer] Rock and rol..")
      bell.synchronized {
        bell.notify()
      }
    }).start()
  }

  case class Friend(name: String) {
    def bow(other:Friend) = {
      this.synchronized {
        println(s"$this: I am bowing to my friend $other")
        other.rise(this)
        println(s"$this: my friend $other has risen")
      }
    }
    def rise(otherFriend: Friend) = {
      this.synchronized {
        println(s"$this: I am rising to my friend $otherFriend")
      }
    }

    var side = "right"
    def switchSide():Unit = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend): Unit = {
      while(this.side == other.side) {
        println(s"$this: oh, but please, $other, feel free to pass ...")
        switchSide()
        Thread.sleep(1000)
      }
    }

  }

  val sam = Friend("Sam")
  val pierre = Friend("Peirrer")
  /*
  new Thread(() => {sam.bow(pierre)}).start()
  new Thread(() => {pierre.bow(sam)}).start()
  */

  new Thread(() => {
    sam.pass(pierre)
  }).start()
  new Thread(() => {
    pierre.pass(sam)
  }).start()

}
