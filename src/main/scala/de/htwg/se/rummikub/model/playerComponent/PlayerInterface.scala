package de.htwg.se.rummikub.model.playerComponent

import de.htwg.se.rummikub.model.{Token, TokenStructure}

trait Player {
  def validateFirstMove: Boolean
  def addToFirstMoveTokens(newTokens: List[Token]): Player
  def deepCopy: Player
  def clusterTokens(tokens: List[Token]): List[TokenStructure]
}