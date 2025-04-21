package de.htwg.se.rummikub.model

enum Color(val value: Int) {
  case RED extends Color(1)
  case BLUE extends Color(2)
  case GREEN extends Color(3)
  case BLACK extends Color(4)

  override def toString: String = this match {
    case RED => "\u001b[31m"
    case BLUE => "\u001b[34m"
    case GREEN => "\u001b[32m"
    case BLACK => "\u001b[30m"
  }

  def reset: String = "\u001b[0m"
}