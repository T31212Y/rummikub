package de.htwg.se.rummikub.model.playerComponent.playerBaseImpl

import de.htwg.se.rummikub.model.playerComponent.{PlayerFactoryInterface, PlayerInterface}
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface

import com.google.inject.Inject

class StandardPlayerFactory @Inject() (tokenStructureFactory: TokenStructureFactoryInterface) extends PlayerFactoryInterface {
  override def createPlayer(name: String): PlayerInterface = new Player(name, tokenStructureFactory = tokenStructureFactory)
}