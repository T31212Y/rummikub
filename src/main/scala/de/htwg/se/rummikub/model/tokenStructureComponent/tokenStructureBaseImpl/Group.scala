package de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

case class Group(group: List[TokenInterface]) extends TokenStructureInterface {

  var tokens: List[TokenInterface] = group

  override def getTokens: List[TokenInterface] = tokens
  override def addToken(token: TokenInterface): Unit = tokens = tokens :+ token
  override def removeToken(token: TokenInterface): Unit = tokens = tokens.filterNot(_.equals(token))

  override def toString: String = tokens.map(_.toString).mkString(" ")

  override def isValid: Boolean = {
    if (tokens.size < 3 || tokens.size > 4) return false

    val (jokers, nonJokers) = tokens.partition(_.isJoker)

    val numbers = nonJokers.flatMap(_.getNumber).distinct
    if (numbers.size != 1) return false

    val usedColors = nonJokers.map(_.getColor).toSet
    if (usedColors.size != nonJokers.size) return false

    val availableColors = Set(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK) -- usedColors
    if (availableColors.size < jokers.size) return false

    true
  }

  def jokerValues: Option[List[Int]] = {
    val distinctNumbers = tokens.filter(_.isNumToken).flatMap(_.getNumber).distinct
    if (distinctNumbers.size == 1) {
      val jokerCount = tokens.count(_.isJoker)
      Some(List.fill(jokerCount)(distinctNumbers.head))
    } else None
  }

  override def points: Int = {
    jokerValues match {
      case Some(jVals) =>
        val jokerIterator = jVals.iterator
        tokens.map {
          case token if token.isNumToken => token.getNumber.get
          case token if token.isJoker    => jokerIterator.next()
        }.sum
      case None =>
        tokens.collect { case token if token.isNumToken => token.getNumber.get }.sum
    }
  }
}
