package de.htwg.se.rummikub.model.playerComponent.playerBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.{TokenStructureInterface, TokenStructureFactoryInterface}

case class Player(
  name: String,
  tokens: List[TokenStructureInterface] = List(),
  commandHistory: List[String] = List(),
  firstMoveTokens: List[TokenStructureInterface] = List(),
  hasCompletedFirstMove: Boolean = false,
  tokenStructureFactory: TokenStructureFactoryInterface // Factory als Feld
) extends PlayerInterface {

  override def toString: String = {
    s"Player: $name"
  }

  override def validateFirstMove: Boolean = {
    val structures = clusterTokens(firstMoveTokens)

    if (structures.isEmpty) {
      println(s"$name's first move is invalid: cannot form valid groups or rows")
      return false
    }

    val totalPoints = structures.map(_.points).sum

    if (totalPoints < 30) {
      println(s"$name's first move must have a total of at least 30 points. You only have $totalPoints.")
      false
    } else true
  }

  override def addToFirstMoveTokens(newTokens: List[TokenStructureInterface]): Player = {
    this.copy(firstMoveTokens = this.firstMoveTokens ++ newTokens)
  }

  override def deepCopy: Player = this.copy(
    tokens = this.tokens.map(identity),
    firstMoveTokens = this.firstMoveTokens.map(identity),
    commandHistory = this.commandHistory.map(identity)
  )

  override def clusterTokens(tokens: List[TokenStructureInterface]): List[TokenStructureInterface] = {

    def backtrack(remaining: List[TokenStructureInterface], acc: List[TokenStructureInterface]): Option[List[TokenStructureInterface]] = {
      if (remaining.isEmpty) {
        Some(acc)
      } else {
        val minSize = 3
        val maxSize = Math.min(remaining.size, 13)

        val possibleSets = for {
          size <- minSize to maxSize
          subset <- remaining.combinations(size)
        } yield {
          val ts = subset.toList
          val tokenList = ts.flatMap(_.getTokens)
          val group: TokenStructureInterface = tokenStructureFactory.createGroup(tokenList)
          val row: TokenStructureInterface = tokenStructureFactory.createRow(tokenList)
          List(group, row).filter(_.isValid).map(validSet => (validSet, ts))
        }

        val flatPossibleSets = possibleSets.flatten.iterator

        flatPossibleSets.flatMap { case (set, ts) =>
          val newRemaining = remaining.diff(ts)
          backtrack(newRemaining, acc :+ set)
        }.toSeq.headOption
      }
    }

    backtrack(tokens, List()).getOrElse(List())
  }
}