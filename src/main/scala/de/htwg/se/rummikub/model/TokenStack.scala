package de.htwg.se.rummikub.model

import scala.util.Random

case class TokenStack() {
  private val tokenFactory: TokenFactory = new StandardTokenFactory
  val allTokens = tokenFactory.generateAllTokens()

  var tokens: List[Token] = Random.shuffle(allTokens)

  def drawToken(): Token = {
    val token = tokens.head
    tokens = tokens.tail
    token
  }

  def drawMultipleTokens(n: Int): List[Token] = {
    (1 to n).map(_ => drawToken()).toList
  }

  def removeToken(token: Token): List[Token] = {
    tokens = tokens.filterNot(_.equals(token))
    tokens
  }

  def removeMultipleTokens(n: Int): List[Token] = {
    val tokensToRemove = tokens.take(n)
    tokens = tokens.drop(n)
    tokensToRemove
  }

  def removeAllTokens(): List[Token] = {
    tokens = List()
    tokens
  }

  def isEmpty: Boolean = tokens.isEmpty

  def size: Int = tokens.size

  override def toString(): String = {
    tokens.map(_.toString).mkString(", ")
  }
}