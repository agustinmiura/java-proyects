package com.rockthejvm.practice

import com.rockthejvm.practice.SocialNetwork.{add, friend, friendQty, mostFriends, noFriendsQty, remove, socialConnection, unfriending}

import scala.annotation.tailrec

object SocialNetwork {
  def add(network: Map[String, Set[String]], person:String): Map[String, Set[String]] =
    network + (person -> Set())
  def friend(network: Map[String, Set[String]], a: String, b:String): Map[String, Set[String]] = {
    val friendsA = network(a)
    val friendsB = network(b)
    network + (a -> (friendsA + b)) + (b -> (friendsB + a))
  }
  def unfriending(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] = {
    val friendsA = network(a)
    val friendsB = network(b)
    network + (a -> (friendsA - b)) + (b -> (friendsB - a))
  }
  def remove(network: Map[String, Set[String]], person:String): Map[String, Set[String]] = {
    def removeAux(friends: Set[String], networkAcum: Map[String,Set[String]]): Map[String, Set[String]] = {
      if (friends.isEmpty) networkAcum
      else removeAux(friends.tail, unfriending(networkAcum, person, friends.head))
    }
    val unfriended = removeAux(network(person), network)
    unfriended - person
  }
  def friendQty(network:Map[String, Set[String]], person:String): Int = {
    if (!network.contains(person)) 0
    else (network(person).size)
  }
  def mostFriends(network:Map[String,Set[String]]): String = {
    network.maxBy(pair => pair._2.size)._1
  }
  def noFriendsQty(network:Map[String, Set[String]]): Int = {
    network.count(p => p._2.size == 0)
  }
  def socialConnection(network:Map[String, Set[String]], a:String, b:String): Boolean = {
    @tailrec
    def bfs(target:String, consideredPeople: Set[String], discoveredPeople: Set[String]): Boolean = {
      if (discoveredPeople.isEmpty) false
      else {
        val person = discoveredPeople.head
        if (person == target) true
        else if (discoveredPeople.contains(person)) bfs(target, consideredPeople, discoveredPeople.tail)
        else bfs(target, discoveredPeople + person, discoveredPeople.tail ++ network(person))
      }
    }
    bfs(b, Set(), network(a) + a)
  }

}

object SocialNetworkTest extends App {

  val empty: Map[String, Set[String]] = Map()
  val network = SocialNetwork.add(add(empty, "Bob"), "Mary")
  println(network)

  println(friend(network, "Bob", "Mary"))

  println(unfriending(friend(network, "Bob", "Mary"), "Bob", "Mary"))

  println(remove(friend(network, "Bob", "Mary"), "Bob"))

  val people = add(add(add(empty, "Bob"), "Mary"), "Jim")
  val jimBob = friend(people, "Bob", "Jim")
  val testNet = friend(people, "Bob", "Mary")

  println(testNet)
  println(friendQty(testNet, "Bob"))
  println(mostFriends(testNet))
  println(noFriendsQty(testNet))
  println(socialConnection(network, "Bob", "Mary"))

}
