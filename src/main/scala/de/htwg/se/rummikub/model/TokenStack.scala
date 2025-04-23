package de.htwg.se.rummikub.model

import scala.util.Random

case class TokenStack() {
  val allTokens = List[Token | Joker] (
  Token(1, Color.RED), Token(2, Color.RED), Token(3, Color.RED), Token(4, Color.RED), Token(5, Color.RED), Token(6, Color.RED), Token(7, Color.RED), Token(8, Color.RED), Token(9, Color.RED), Token(10, Color.RED), Token(11, Color.RED), Token(12, Color.RED), Token(13, Color.RED),
  Token(1, Color.RED), Token(2, Color.RED), Token(3, Color.RED), Token(4, Color.RED), Token(5, Color.RED), Token(6, Color.RED), Token(7, Color.RED), Token(8, Color.RED), Token(9, Color.RED), Token(10, Color.RED), Token(11, Color.RED), Token(12, Color.RED), Token(13, Color.RED),
  Token(1, Color.BLUE), Token(2, Color.BLUE), Token(3, Color.BLUE), Token(4, Color.BLUE), Token(5, Color.BLUE), Token(6, Color.BLUE), Token(7, Color.BLUE), Token(8, Color.BLUE), Token(9, Color.BLUE), Token(10, Color.BLUE), Token(11, Color.BLUE), Token(12, Color.BLUE), Token(13, Color.BLUE),
  Token(1, Color.BLUE), Token(2, Color.BLUE), Token(3, Color.BLUE), Token(4, Color.BLUE), Token(5, Color.BLUE), Token(6, Color.BLUE), Token(7, Color.BLUE), Token(8, Color.BLUE), Token(9, Color.BLUE), Token(10, Color.BLUE), Token(11, Color.BLUE), Token(12, Color.BLUE), Token(13, Color.BLUE),
  Token(1, Color.GREEN), Token(2, Color.GREEN), Token(3, Color.GREEN), Token(4, Color.GREEN), Token(5, Color.GREEN), Token(6, Color.GREEN), Token(7, Color.GREEN), Token(8, Color.GREEN), Token(9, Color.GREEN), Token(10, Color.GREEN), Token(11, Color.GREEN), Token(12, Color.GREEN), Token(13, Color.GREEN),
  Token(1, Color.GREEN), Token(2, Color.GREEN), Token(3, Color.GREEN), Token(4, Color.GREEN), Token(5, Color.GREEN), Token(6, Color.GREEN), Token(7, Color.GREEN), Token(8, Color.GREEN), Token(9, Color.GREEN), Token(10, Color.GREEN), Token(11, Color.GREEN), Token(12, Color.GREEN), Token(13, Color.GREEN),
  Token(1, Color.BLACK), Token(2, Color.BLACK), Token(3, Color.BLACK), Token(4, Color.BLACK), Token(5, Color.BLACK), Token(6, Color.BLACK), Token(7, Color.BLACK), Token(8, Color.BLACK), Token(9, Color.BLACK), Token(10, Color.BLACK), Token(11, Color.BLACK), Token(12, Color.BLACK), Token(13, Color.BLACK),
  Token(1, Color.BLACK), Token(2, Color.BLACK), Token(3, Color.BLACK), Token(4, Color.BLACK), Token(5, Color.BLACK), Token(6, Color.BLACK), Token(7, Color.BLACK), Token(8, Color.BLACK), Token(9, Color.BLACK), Token(10, Color.BLACK), Token(11, Color.BLACK), Token(12, Color.BLACK), Token(13, Color.BLACK),
  Joker(Color.RED), Joker(Color.BLACK)
  )

  var tokens: List[Token | Joker] = Random.shuffle(allTokens)

  def drawToken(): Token | Joker = {
    val token = tokens.head
    tokens = tokens.tail
    token
  }

  def drawMultipleTokens(n: Int): List[Token | Joker] = {
    (1 to n).map(_ => drawToken()).toList
  }

  def removeToken(token: Token | Joker): List[Token | Joker] = {
    tokens = tokens.filterNot(_.equals(token))
    tokens
  }

  def removeAllTokens(): List[Token | Joker] = {
    tokens = List()
    tokens
  }

  def isEmpty: Boolean = tokens.isEmpty

  def size: Int = tokens.size

  override def toString(): String = {
    tokens.map(_.toString).mkString(", ")
  }
}