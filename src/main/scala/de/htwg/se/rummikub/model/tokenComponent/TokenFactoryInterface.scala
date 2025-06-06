package de.htwg.se.rummikub.model.tokenComponent

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}

trait TokenFactoryInterface {
  def createNumToken(number: Int, color: Color): TokenInterface
  def createJoker(color: Color): TokenInterface
  def generateAllTokens(): List[TokenInterface]
}