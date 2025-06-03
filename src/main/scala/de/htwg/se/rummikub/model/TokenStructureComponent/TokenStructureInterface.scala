package de.htwg.se.rummikub.model.tokenStructureComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

abstract class TokenStructureInterface(initial: List[TokenInterface]) {

  protected var tokens: List[TokenInterface] = initial

  def getTokens: List[TokenInterface] = tokens
  def addToken(token: TokenInterface): Unit = tokens :+= token
  def removeToken(token: TokenInterface): Unit = tokens = tokens.filterNot(_.equals(token))

  override def toString: String = tokens.map(_.toString).mkString(" ")

  def isValid: Boolean
  def points: Int
}
