package de.htwg.se.rummikub.model.tokenComponent

trait TokenInterface {
  def isJoker: Boolean
  def isNumToken: Boolean
  def number: Option[Int]
  def color: Option[Color]
  override def toString: String
}