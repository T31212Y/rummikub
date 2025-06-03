package de.htwg.se.rummikub.model.tokenComponent

trait TokenFactoryInterface {
  def createNumToken(number: Int, color: Color): TokenInterface
  def createJoker(color: Color): TokenInterface
  def generateAllTokens: List[TokenInterface]
}