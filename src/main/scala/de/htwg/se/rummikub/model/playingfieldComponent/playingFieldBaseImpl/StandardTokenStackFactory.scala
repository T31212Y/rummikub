package de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingfieldComponent.{TokenStackInterface, TokenStackFactoryInterface}

class StandardTokenStackFactory extends TokenStackFactoryInterface {
  override def create: TokenStackInterface = TokenStack.apply()
}