
import scala.annotation.tailrec

object DefaultArgs extends App {

  @tailrec
  def factorial(n: Int, acum: Int = 1): Int =
    if (n <= 1) acum
    else factorial(n - 1, n * acum)

  val fact10 = factorial(10, 2)
  println(" Factorial of 10 is " + fact10)

  def savePicture(format: String = "jpg", width: Int = 640, height: Int = 480): Unit = println(" Saving picture ")

  savePicture("JPG", 640, 480)
  savePicture()
  savePicture(height = 500)
  savePicture(height = 600, width = 800, format = "bmp")
}
