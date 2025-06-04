package de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.{NumToken, Joker}
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

case class Group(group: List[TokenInterface]) extends TokenStructureInterface {

    var tokens: List[TokenInterface] = group

    override def getTokens: List[TokenInterface] = tokens
    override def addToken(token: TokenInterface): Unit = tokens = tokens :+ token
    override def removeToken(token: TokenInterface): Unit = tokens = tokens.filterNot(_.equals(token))

    override def toString: String = {
        tokens.map(_.toString).mkString(" ")
    }

    override def isValid: Boolean = {
        if (tokens.size < 3 || tokens.size > 4) return false

        val (jokers, nonJokers) = tokens.partition {
        case _: Joker => true
        case _        => false
        }

        val numTokens = nonJokers.collect {
        case NumToken(n, c) => (n, c)
        }

        val distinctNumbers = numTokens.map(_._1).distinct
        if (distinctNumbers.size != 1) return false

        val usedColors = numTokens.map(_._2).toSet

        val allColors = Set(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK)
        val availableColorsForJokers = allColors -- usedColors

        true
    }

    def jokerValues: Option[Int] = {
        val numTokens = tokens.collect { case NumToken(n, _) => n }.distinct
        if (numTokens.size == 1) Some(numTokens.head) else None
    }

    override def points: Int = {
        val jVal = jokerValues.getOrElse(0)
        tokens.map {
        case NumToken(n, _) => n
        case _: Joker       => jVal
        }.sum
    }
}