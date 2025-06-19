package de.htwg.se.rummikub.model.playerComponent.playerBaseImpl

import de.htwg.se.rummikub.model.playerComponent.{PlayerFactoryInterface, PlayerInterface}
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface

class StandardPlayerFactory (using tokenStructureFactory: TokenStructureFactoryInterface) extends PlayerFactoryInterface {
  override def createPlayer(name: String): PlayerInterface = new Player(name)
}