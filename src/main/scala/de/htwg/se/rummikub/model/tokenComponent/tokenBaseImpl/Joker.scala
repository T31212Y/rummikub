package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}

case class Joker(col: Color) extends TokenInterface {
  override def isJoker: Boolean = true
  override def color: Option[Color] = Some(col)
  override def toString: String = s" ${col.toString}J${col.reset}"

  override def isNumToken: Boolean = false
  override def number: Option[Int] = None
}