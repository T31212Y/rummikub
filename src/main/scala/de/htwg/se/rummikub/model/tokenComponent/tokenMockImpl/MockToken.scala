package de.htwg.se.rummikub.model.tokenComponent.tokenMockImpl

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}

case class MockToken(number: Option[Int], color: Color) extends TokenInterface {
  override def getNumber: Option[Int] = number
  override def getColor: Color = color

  override def toString: String = s"MockToken(number=$number, color=$color)"
}