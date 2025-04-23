package de.htwg.se.rummikub.model

case class Player(name: String) {

  var tokens: List[Token | Joker] = List()
  var commandHistory: List[String] = List()


  override def toString: String = {
    s"Player: $name"
  }
}