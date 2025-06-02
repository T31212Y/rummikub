package de.htwg.se.rummikub.model.playerComponent

import de.htwg.se.rummikub.model.{Token, TokenStructure}

trait PlayerInterface {
  def validateFirstMove: Boolean
  def addToFirstMoveTokens(newTokens: List[Token]): PlayerInterface
  def deepCopy: PlayerInterface
  def clusterTokens(tokens: List[Token]): List[TokenStructure]
}