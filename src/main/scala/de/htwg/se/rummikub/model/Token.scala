package de.htwg.se.rummikub.model

case class Token(number: Int, color: String) {
  override def toString: String = s"$number $color"
}