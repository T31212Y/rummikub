package de.htwg.se.rummikub.model

object TokenFactory {
  def createToken(kind: String, number: Int, color: Color) = kind match {
      case "Joker" => new Joker(color)
      case "NumToken" => new NumToken(number, color)
  }

  def generateAllTokens(): List[Token] = {
    val colors = List(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK)
    val numbers = 1 to 13
    val tokens = for {
      color <- colors
      number <- numbers
      _ <- 1 to 2
    } yield createToken("NumToken", number, color)

    tokens.toList ++ List(
        createToken("Joker", 0, Color.RED),
        createToken("Joker", 0, Color.BLACK)
    )
  }
}