package de.htwg.se.rummikub.model.tokenComponent

trait TokenInterface {
  def getNumber: Option[Int]
  def getColor: Color

  override def toString: String
}