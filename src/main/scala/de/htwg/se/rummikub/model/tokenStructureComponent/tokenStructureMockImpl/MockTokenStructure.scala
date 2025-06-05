package de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureMockImpl

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

class MockTokenStructure(initial: List[TokenInterface] = List.empty) extends TokenStructureInterface {

  var tokens: List[TokenInterface] = initial

  override def getTokens: List[TokenInterface] = tokens

  override def addToken(token: TokenInterface): Unit = {
    tokens = tokens :+ token
  }

  override def removeToken(token: TokenInterface): Unit = {
    tokens = tokens.filterNot(_.equals(token))
  }

  override def toString: String = s"MockTokenStructure(tokens=${tokens.mkString(", ")})"

  override def isValid: Boolean = {
    tokens.length >= 3
  }

  override def points: Int = {
    tokens.flatMap(_.getNumber).sum
  }
}