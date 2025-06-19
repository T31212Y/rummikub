package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}

case class Joker(color: Color) extends TokenInterface {
  override def getNumber: Option[Int] = None
  override def getColor: Color = color

  override def isJoker: Boolean = true
  override def isNumToken: Boolean = false

  override def toString: String = s" ${color.toString}J${color.reset}"
}