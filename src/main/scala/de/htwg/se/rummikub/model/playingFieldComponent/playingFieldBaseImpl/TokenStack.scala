package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import scala.util.Random

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.StandardTokenFactory

case class TokenStack(tokens: List[TokenInterface]) {

  def drawToken(): (TokenInterface, TokenStack) = {
    val token = tokens.head
    val updatedStack = TokenStack(tokens.tail)
    (token, updatedStack)
  }

  def drawMultipleTokens(n: Int): (List[TokenInterface], TokenStack) = {
    val drawn = tokens.take(n)
    val updatedStack = TokenStack(tokens.drop(n))
    (drawn, updatedStack)
  }

  def removeToken(token: TokenInterface): TokenStack =
    TokenStack(tokens.filterNot(_ == token))

  def removeMultipleTokens(n: Int): (List[TokenInterface], TokenStack) = {
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