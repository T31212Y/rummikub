package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import scala.util.Random

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackInterface

case class TokenStack(tokens: List[TokenInterface]) extends TokenStackInterface {

  override def getTokens: List[TokenInterface] = tokens

  override def drawToken: (TokenInterface, TokenStackInterface) = {
    val token = tokens.head
    val updatedStack = TokenStack(tokens.tail)
    (token, updatedStack)
  }

  override def drawMultipleTokens(n: Int): (List[TokenInterface], TokenStackInterface) = {
    val drawn = tokens.take(n)
    val updatedStack = TokenStack(tokens.drop(n))
    (drawn, updatedStack)
  }

  override def removeToken(token: TokenInterface): TokenStackInterface = TokenStack(tokens.filterNot(_ == token))

  override def removeMultipleTokens(n: Int): (List[TokenInterface], TokenStackInterface) = {
    val removed = tokens.take(n)
    val updatedStack = TokenStack(tokens.drop(n))
    (removed, updatedStack)
  }

  override def removeAllTokens: TokenStackInterface = TokenStack(List())

  override def isEmpty: Boolean = tokens.isEmpty

  override def size: Int = tokens.size

  override def toString: String = tokens.map(_.toString).mkString(", ")
}