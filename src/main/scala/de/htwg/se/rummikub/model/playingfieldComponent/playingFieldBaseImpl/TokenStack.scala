package de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl

import scala.util.Random

import de.htwg.se.rummikub.model.TokenStructureComponent.TokenStructureInterface

case class TokenStack(tokens: List[TokenStructureInterface]) {

  def drawToken(): (TokenStructureInterface, TokenStack) = {
    val token = tokens.head
    val updatedStack = TokenStack(tokens.tail)
    (token, updatedStack)
  }

  def drawMultipleTokens(n: Int): (List[TokenStructureInterface], TokenStack) = {
    val drawn = tokens.take(n)
    val updatedStack = TokenStack(tokens.drop(n))
    (drawn, updatedStack)
  }

  def removeToken(token: TokenStructureInterface): TokenStack =
    TokenStack(tokens.filterNot(_ == token))

  def removeMultipleTokens(n: Int): (List[TokenStructureInterface], TokenStack) = {
    val removed = tokens.take(n)
    val updatedStack = TokenStack(tokens.drop(n))
    (removed, updatedStack)
  }

  def removeAllTokens(): TokenStack =
    TokenStack(List())

  def isEmpty: Boolean = tokens.isEmpty

  def size: Int = tokens.size

  override def toString: String =
    tokens.map(_.toString).mkString(", ")
}

object TokenStack {
  def apply(): TokenStack = {
    val tokenFactory = new StandardTokenFactory
    val allTokens = Random.shuffle(tokenFactory.generateAllTokens())
    new TokenStack(allTokens)
  }
}