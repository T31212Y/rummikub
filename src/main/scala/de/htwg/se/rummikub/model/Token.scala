package de.htwg.se.rummikub.model

trait Token {
  override def toString: String
}

case class NumToken(number: Int, color: Color) extends Token {
  override def toString: String = f"${color.toString}$number%2d${color.reset}"

  override def equals(obj: Any): Boolean = obj match {
    case that: NumToken => this.number == that.number && this.color == that.color
    case _ => false
  }

  override def hashCode(): Int = (number, color).##
}

case class Joker(color: Color) extends Token {
  override def toString: String = s" ${color.toString}J${color.reset}"

  override def equals(obj: Any): Boolean = obj match {
    case that: Joker => this.color == that.color
    case _ => false
  }

  override def hashCode(): Int = color.##
}