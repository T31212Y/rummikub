package de.htwg.se.rummikub.model.playerComponent.playerBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.{TokenStructureInterface, TokenStructureFactoryInterface}

import com.google.inject.Inject

case class Player @Inject() (name: String, 
  tokens: List[TokenInterface] = List(), 
  commandHistory: List[String] = List(), 
  firstMoveTokens: List[TokenInterface] = List(), 
  hasCompletedFirstMove: Boolean = false,
  tokenStructureFactory: TokenStructureFactoryInterface
) extends PlayerInterface {

  override def toString: String = {
    s"Player: $name"
  }

  override def validateFirstMove: Boolean = {
    val structures = clusterTokens(firstMoveTokens)
    println(s"Clustered structures: ${structures.map(_.toString).mkString("; ")}")


    if (structures.isEmpty) {
      println(s"$name's first move is invalid: cannot form valid groups or rows")
      return false
    }

    val totalPoints = structures.map(_.points).sum
    println(s"Total points calculated: $totalPoints")

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

  def clusterTokens(tokens: List[TokenInterface]): List[TokenStructureInterface] = {
    val minSize = 3
    val maxSize = Math.min(tokens.size, 13)

    val validSets = for {
      size <- minSize to maxSize
      subset <- tokens.combinations(size)
      group = tokenStructureFactory.createGroup(subset)
      row = tokenStructureFactory.createRow(subset)
      validSet <- List(group, row).filter(_.isValid)
    } yield validSet

    validSets.toList.sortBy(- _.points).take(1)
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
    copy(tokens = newTokens, commandHistory = newCommandHistory, hasCompletedFirstMove = newHasCompletedFirstMove, firstMoveTokens = this.firstMoveTokens)
  }
}