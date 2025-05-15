package de.htwg.se.rummikub.model

case class NumToken(number: Int, color: Color) extends Token {
    override def toString: String = f"${color.toString}$number%2d${color.reset}"
  }