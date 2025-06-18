package de.htwg.se.rummikub.model.playerComponent.playerBaseImpl

import de.htwg.se.rummikub.model.playerComponent.{PlayerFactoryInterface, PlayerInterface}

class StandardPlayerFactory extends PlayerFactoryInterface {
  override def createPlayer(name: String): PlayerInterface = new Player(name)
}