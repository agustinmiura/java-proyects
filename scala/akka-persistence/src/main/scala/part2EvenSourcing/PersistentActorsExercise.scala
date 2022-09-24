package part2EvenSourcing

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.PersistentActor

import scala.collection.mutable

object PersistentActorsExercise extends App {

  /**
    *
    * citizens
    * poll
    *
    * @param citizenId
    * @param candidate
    */
  case class VoteRecorded(citizenId: String, candidate: String)

  case class Vote(citizenId: String, candidate: String)

  case object Info

  case object Shutdown

  class VotingMachine extends PersistentActor with ActorLogging {

    val citizens: mutable.Set[String] = new mutable.HashSet[String]()
    val poll: mutable.Map[String, Int] = new mutable.HashMap[String, Int]()

    override def persistenceId: String = "voting-machine"

    override def receiveCommand: Receive = {
      case vote@Vote(citizenId, candidate) =>
        if (!citizens.contains(vote.citizenId))
          persist(vote) { _ =>
            log.info(s"Persisted : $vote")
            handleInternalState(vote)
          }
      case Info =>
        log.info(s"Current state $citizens, $poll")
    }

    def handleInternalState(vote: Vote): Unit = {
      if (!citizens.contains(vote.citizenId)) {
        citizens.add(vote.citizenId)
        val votes = poll.getOrElse(vote.candidate, 0) + 1
        poll.put(vote.candidate, votes)
      }
    }

    override def receiveRecover: Receive = {
      case Vote(citizen, candidate) =>
        log.info(s"$citizen, $candidate")
        val vote = Vote(citizen, candidate)
        handleInternalState(vote)
    }
  }

  val votingSystem = ActorSystem("VotingMachine")
  val votingMachine = votingSystem.actorOf(Props[VotingMachine], "votingMachine")

  val votesMap = Map[String, String](
    "Alice" -> "Martin",
    "Bob" -> "Roland",
    "Charlie" -> "Martin",
    "Charlie" -> "Martin"
  )

  votesMap.keys.foreach { citizen =>
    votingMachine ! Vote(citizen, votesMap(citizen))
  }

  votingMachine ! Info
}
