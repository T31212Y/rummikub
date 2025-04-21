package de.htwg.se.rummikub.model

case class Joker(color: Color) {
  override def toString(): String = s" ${color.toString}J${color.reset}"  // 🌝
}