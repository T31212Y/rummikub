package de.htwg.se.rummikub.model.tokenComponent

trait TokenInterface {
  def getNumber: Option[Int]
  def getColor: Color

  def isJoker: Boolean
  def isNumToken: Boolean
  def serialize: String


  override def toString: String
}