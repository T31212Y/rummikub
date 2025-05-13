package de.htwg.se.rummikub.model

trait Token {
  override def toString: String
}

case class NumToken(number: Int, color: Color) extends Token {
  override def toString: String = f"${color.toString}$number%2d${color.reset}"
}

case class Joker(color: Color) extends Token {
  override def toString: String = s" ${color.toString}J${color.reset}"  // 🌝
}