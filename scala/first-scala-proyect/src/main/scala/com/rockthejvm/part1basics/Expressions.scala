package com.rockthejvm.part1basics


object Expressions extends App {

  println(" Expressions example ")

  val x = 1 + 2

  println(x)

  println(2 + 3 + 4)

  println(1 == x)

  println(1 != x)

  println(!(1 == x))

  println(1 > 2 && 2 < 3)

  var aVariable = 2
  aVariable += 3
  aVariable -= 3

  val aCondition = true
  val aConditionValue = if (aCondition) {
    5
  } else {
    3
  }
  println(aConditionValue)
  println(if (aCondition) {
    5
  } else {
    3
  })

  val aWeirdValue = (aVariable = 3)
  println(aWeirdValue)

  val aCodeBlock = {
    val y = 2
    val z = y + 1
    if (z > 2) {
      "hello"
    } else {
      "goodbye"
    }
  }

  println(aCodeBlock)


}
