package de.htwg.se.rummikub.model.tokenStructureComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

trait TokenStructureInterface {
  def getTokens: List[TokenInterface]
  def addToken(token: TokenInterface): TokenStructureInterface
  def removeToken(token: TokenInterface): TokenStructureInterface

  def isValid: Boolean
  def points: Int

  override def toString: String = getTokens.map(_.toString).mkString(" ")
}
