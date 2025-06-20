package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}

case class NumToken(number: Int, color: Color) extends TokenInterface {
  override def getNumber = Some(number)
  override def getColor = color

  override def isJoker: Boolean = false
  override def isNumToken: Boolean = true

  override def toString: String = f"${color.toString}$number%2d${color.reset}"
}