package de.htwg.se.rummikub.model.playerComponent.playerBaseImpl

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.Group
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.Row

case class Player(name: String, 
  tokens: List[TokenInterface] = List(), 
  commandHistory: List[String] = List(), 
  firstMoveTokens: List[TokenInterface] = List(), 
  hasCompletedFirstMove: Boolean = false
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

  override def addToFirstMoveTokens(newTokens: List[TokenInterface]): Player = {
    this.copy(firstMoveTokens = this.firstMoveTokens ++ newTokens)
  }

  override def deepCopy: Player = this.copy(
    tokens = this.tokens.map(identity),
    firstMoveTokens = this.firstMoveTokens.map(identity),
    commandHistory = this.commandHistory.map(identity)
  )

  override def clusterTokens(tokens: List[TokenInterface]): List[TokenStructureInterface] = {
    
    def backtrack(remaining: List[TokenInterface], acc: List[TokenStructureInterface]): Option[List[TokenStructureInterface]] = {
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
          val group = Group(ts)
          val row = Row(ts)
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

  override def getName: String = {
    name
  }

  override def getTokens: List[TokenInterface] = {
    tokens
  }

  override def getCommandHistory: List[String] = {
    commandHistory
  }

  override def getFirstMoveTokens: List[TokenInterface] = {
    firstMoveTokens
  }

  override def getHasCompletedFirstMove: Boolean = {
    hasCompletedFirstMove
  }

  override def updated(newTokens: List[TokenInterface], newCommandHistory: List[String], newHasCompletedFirstMove: Boolean): PlayerInterface = {
    copy(tokens = newTokens, commandHistory = newCommandHistory, hasCompletedFirstMove = newHasCompletedFirstMove)
  }
}