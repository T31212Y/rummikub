package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

case class NumToken(number: Int, color: Color) extends TokenInterface {
  override def toString: String = f"${color.toString}$number%2d${color.reset}"
}