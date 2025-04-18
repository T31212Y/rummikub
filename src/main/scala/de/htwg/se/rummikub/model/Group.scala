package de.htwg.se.rummikub.model

case class Group(var tokens: List[Token]) {
  def addToken(token: Token): Unit = {
    tokens = tokens :+ token
  }

  def removeToken(token: Token): Unit = {
    tokens = tokens.filterNot(_.equals(token))
  }

  def getTokens: List[Token] = tokens

  override def toString: String = {
    tokens.map(_.toString).mkString(", ")
  }
}