package de.htwg.se.rummikub.model.playerComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

trait PlayerInterface {
  def name: String 
  def validateFirstMove: Boolean
  def addToFirstMoveTokens(newTokens: List[TokenInterface]): PlayerInterface
  def deepCopy: PlayerInterface
  def clusterTokens(tokens: List[TokenInterface]): List[TokenStructureInterface]
  def getTokens: List[TokenStructureInterface]
  def withTokens(tokens: List[TokenStructureInterface]): PlayerInterface
  def hasCompletedFirstMove: Boolean
  def clearCommandHistory: PlayerInterface
  def withCompletedFirstMove(flag: Boolean): PlayerInterface


}