package de.htwg.se.rummikub.model

case class Token(number: Int, color: Color) {
  override def toString: String = f"${color.toString}$number%2d${color.reset}"
}