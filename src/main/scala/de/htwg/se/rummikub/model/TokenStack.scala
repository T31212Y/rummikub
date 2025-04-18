package de.htwg.se.rummikub.model

case class TokenStack(var tokens: List[Token | Joker]) {
  def removeToken(token: Token | Joker): Token | Joker = {
    tokens = tokens.filterNot(_.equals(token))
    return token
  }

  def isEmpty: Boolean = tokens.isEmpty

  def size: Int = tokens.size
}