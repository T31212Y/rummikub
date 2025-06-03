package de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingfieldComponent.TokenStackInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.StandardTokenFactory
import scala.util.Random

case class TokenStack(tokens: List[TokenStructureInterface]) extends TokenStackInterface {

  def drawToken(): (TokenStructureInterface, TokenStackInterface) = {
    val token = tokens.head
    val updatedStack = TokenStack(tokens.tail)
    (token, updatedStack)
  }

  def drawMultipleTokens(n: Int): (List[TokenStructureInterface], TokenStackInterface) = {
    val drawn = tokens.take(n)
    val updatedStack = TokenStack(tokens.drop(n))
    (drawn, updatedStack)
  }

  def removeToken(token: TokenStructureInterface): TokenStackInterface =
    TokenStack(tokens.filterNot(_ == token))

  def isEmpty: Boolean = tokens.isEmpty

  def size: Int = tokens.size

  override def toString: String =
    tokens.map(_.toString).mkString(", ")
}

object TokenStack {
  def apply(): TokenStack = {
    val tokenFactory = new StandardTokenFactory
    val allTokens = Random.shuffle(tokenFactory.generateAllTokens)
    val tokenStructures = allTokens.map(_.asInstanceOf[TokenStructureInterface])
    new TokenStack(tokenStructures)
  }
}
