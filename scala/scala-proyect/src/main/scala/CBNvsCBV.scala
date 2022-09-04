package ar.com.name
package ar.com.name

object CBNvsCBV extends App {

  def calledByValue(x: Long): Unit = {
    println("by value " + x)
    println("by value " + x)
  }

  def calledByName(x: => Long): Unit = {
    println("by calledByName " + x)
    println("by calledByName " + x)
  }

  calledByValue(System.nanoTime())
  calledByName(System.nanoTime())

  def infiniteRecursion(): Int = 1 + infiniteRecursion()

  def printFirst(x: Int, y: => Int) = println(x)

  printFirst(34, infiniteRecursion())


}
