package de.htwg.se.rummikub.model

case class Player(name: String, tokens: List[Token | Joker] = List(), commandHistory: List[String] = List()) {
  override def toString: String = {
    s"Player: $name"
  }
}