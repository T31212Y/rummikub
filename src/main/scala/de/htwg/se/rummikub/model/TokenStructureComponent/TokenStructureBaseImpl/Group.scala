package de.htwg.se.rummikub.model.TokenStructureComponent.TokenStructureBaseImpl
import de.htwg.se.rummikub.model.TokenStructureComponent.TokenStructureInterface

import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.{Joker, NumToken}
import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}


case class Group(group: List[TokenInterface]) extends TokenStructureInterface(group) {

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

        val usedColors = numTokens.map(_._2).collect { case c: Color => c }.toSet

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