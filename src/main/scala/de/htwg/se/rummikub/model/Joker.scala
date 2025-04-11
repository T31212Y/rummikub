package de.htwg.se.rummikub.model

case class Joker(color:String) {
  override def toString(): String = s"Joker $color"
}