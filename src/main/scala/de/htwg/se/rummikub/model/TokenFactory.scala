package de.htwg.se.rummikub.model

trait TokenFactory {
  def createNumToken(number: Int, color: Color): Token
  def createJoker(color: Color): Token
  def generateAllTokens(): List[Token]
}


class StandardTokenFactory extends TokenFactory {
  override def createNumToken(number: Int, color: Color): Token = new NumToken(number, color)
  override def createJoker(color: Color): Token = new Joker(color)

  override def generateAllTokens(): List[Token] = {
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