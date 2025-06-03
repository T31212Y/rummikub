package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}

case class NumToken(num: Int, col: Color) extends TokenInterface {
  override def isNumToken: Boolean = true
  override def number: Option[Int] = Some(num)
  override def color: Option[Color] = Some(col)

  override def toString: String = f"${col.toString}$num%2d${col.reset}"

  override def isJoker: Boolean = false
}