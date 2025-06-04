package de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.{NumToken, Joker}
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface
 
case class Row(row: List[TokenInterface]) extends TokenStructureInterface {

    var tokens: List[TokenInterface] = row

    override def getTokens: List[TokenInterface] = tokens
    override def addToken(token: TokenInterface): Unit = tokens = tokens :+ token
    override def removeToken(token: TokenInterface): Unit = tokens = tokens.filterNot(_.equals(token))

    override def toString: String = {
        tokens.map(_.toString).mkString(" ")
    }

    override def isValid: Boolean = {
        if (tokens.size < 3 || tokens.size > 13) return false

        val (jokers, nonJokers) = tokens.partition(_.isInstanceOf[Joker])
        val numTokens = nonJokers.collect {
            case NumToken(n, c) => (n, c) 
        }

        if (!hasUniformColor(numTokens)) return false

        val nums = numTokens.map(_._1).distinct.sorted
        val totalLength = nums.length + jokers.length

        hasValidSequenceWithJokers(nums, jokers.length, totalLength)
    }

    private def hasUniformColor(tokens: List[(Int, Color)]): Boolean = {
        tokens.map(_._2).distinct.size == 1
    }

    private def generateCyclicSequence(start: Int, length: Int): List[Int] = {
        LazyList.iterate(start)(_ % 13 + 1).take(length).toList
    }

    private def countMissingValues(targetSeq: List[Int], actualValues: List[Int]): Int = {
        val missing = targetSeq.diff(actualValues)
        if ((actualValues.toSet & missing.toSet).nonEmpty) Int.MaxValue else missing.size
    }

    private def hasValidSequenceWithJokers(values: List[Int], jokers: Int, totalLength: Int): Boolean = {
        (1 to 13).exists { start =>
        val sequence = generateCyclicSequence(start, totalLength)
        val neededJokers = countMissingValues(sequence, values)
        neededJokers <= jokers
        }
    }

    def jokerValues: Option[List[Int]] = {
        val (jokers, nonJokers) = tokens.partition(_.isInstanceOf[Joker])
        val numTokens = nonJokers.collect { case NumToken(n, _) => n }.sorted

        if (numTokens.isEmpty) return None

        val totalLength = tokens.length

        (1 to 13).iterator.map { start =>
            val sequence = generateCyclicSequence(start, totalLength)
            val missing = sequence.diff(numTokens)
            if (missing.size == jokers.size) Some(missing)
            else None
        }.collectFirst { case Some(vals) => vals }
    }

    override def points: Int = {
        jokerValues match {
        case Some(jVals) =>
            tokens.zipAll(jVals, null, 0).map {
            case (NumToken(n, _), _) => n
            case (_: Joker, jVal)    => jVal
            }.sum
        case None =>
            tokens.collect { case NumToken(n, _) => n }.sum
        }
    }
}