package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.TokenFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.{TokenStackInterface, TokenStackFactoryInterface}

import scala.util.Random

import com.google.inject.Inject

class StandardTokenStackFactory @Inject() (tokenFactory: TokenFactoryInterface) extends TokenStackFactoryInterface {
  override def createShuffledStack: TokenStackInterface = {
    val allTokens = Random.shuffle(tokenFactory.generateAllTokens())
    new TokenStack(allTokens)
  }
}