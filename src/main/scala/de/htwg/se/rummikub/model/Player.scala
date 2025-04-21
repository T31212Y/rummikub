package de.htwg.se.rummikub.model

case class Player(name: String, tokens: List[Token | Joker]) {
  override def toString: String = {
    val tokenStrings = tokens.map(_.toString).mkString(", ")
    s"Player: $name, Tokens: [$tokenStrings]"
  }
}