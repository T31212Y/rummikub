package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

case class Joker(color: Color) extends TokenInterface {
  override def toString: String = s" ${color.toString}J${color.reset}"
}