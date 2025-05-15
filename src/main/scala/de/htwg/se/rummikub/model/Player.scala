package de.htwg.se.rummikub.model

case class Player(name: String, tokens: List[Token] = List(), commandHistory: List[String] = List(), firstMoveTokens: List[Token] = List()) {

  override def toString: String = {
    s"Player: $name"
  }

  def validateFirstMove(): Boolean = {
    val totalPoints = firstMoveTokens.collect {
      case NumToken(n, _) => n
      case _: Joker       => 0
    }.sum

    if (totalPoints < 30) {
      println(s"$name's first move must have a total of at least 30 points. You only have $totalPoints.")
      false
    } else {
      true
    }
  }

  def addToFirstMoveTokens(newTokens: List[Token]): Player = {
    this.copy(firstMoveTokens = this.firstMoveTokens ++ newTokens)
  }
}