package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, ColorInterface}

case class NumToken(num: Int, col: ColorInterface) extends TokenInterface {
  override def isNumToken: Boolean = true
  override def number: Option[Int] = Some(num)
  override def color: Option[ColorInterface] = Some(col)

  override def toString: String = f"${col.toString}$num%2d${col.reset}"

  override def isJoker: Boolean = false
}