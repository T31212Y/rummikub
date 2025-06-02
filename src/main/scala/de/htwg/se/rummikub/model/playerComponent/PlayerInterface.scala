package de.htwg.se.rummikub.model.playerComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

import de.htwg.se.rummikub.model.TokenStructure

trait PlayerInterface {
  def validateFirstMove: Boolean
  def addToFirstMoveTokens(newTokens: List[TokenInterface]): PlayerInterface
  def deepCopy: PlayerInterface
  def clusterTokens(tokens: List[TokenInterface]): List[TokenStructure]
}