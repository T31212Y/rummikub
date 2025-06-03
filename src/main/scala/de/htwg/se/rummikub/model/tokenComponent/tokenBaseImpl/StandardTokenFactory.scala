package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color, TokenFactoryInterface}

class StandardTokenFactory extends TokenFactoryInterface {
  override def createNumToken(number: Int, color: Color): TokenInterface = new NumToken(number, color)
  override def createJoker(color: Color): TokenInterface = new Joker(color)

  override def generateAllTokens: List[TokenInterface] = {
    val colors = List(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK)
    val numbers = 1 to 13
    val tokens = for {
      color <- colors
      number <- numbers
      _ <- 1 to 2
    } yield createNumToken(number, color)

    tokens.toList ++ List(
        createJoker(Color.RED),
        createJoker(Color.BLACK)
    )
  }
}