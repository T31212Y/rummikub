package de.htwg.se.rummikub.model

case class Player(name: String) {
  override def toString: String = name
}