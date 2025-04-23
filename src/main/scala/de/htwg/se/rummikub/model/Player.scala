package de.htwg.se.rummikub.model

case class Player(name: String) {

  var tokens: List[Token | Joker] = List()
  var tokensTaken: List[Token | Joker] = List()

  var startCounter = 0
  var commandHistory: List[String] = List()

  def setFirstRowGroup(): Boolean = {
    if (startCounter < 30) {
      false
    } else {
      true
    }
  }

  override def toString: String = {
    val tokenStrings = tokens.map(_.toString).mkString(", ")
    s"Player: $name"
  }
}