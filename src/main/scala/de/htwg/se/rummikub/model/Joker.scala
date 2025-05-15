package de.htwg.se.rummikub.model

case class Joker(color: Color) extends Token {
    override def toString: String = s" ${color.toString}J${color.reset}"
  }