package de.htwg.se.rummikub.model.gameModeComponent.gameMode4PlayerImpl

import de.htwg.se.rummikub.model.gameModeComponent.{PlayerModeFactoryInterface, GameModeInterface}

class FourPlayerModeFactory extends PlayerModeFactoryInterface {
  override def supportedPlayerCount: Int = 4
  override def create(playerNames: List[String]): GameModeInterface = new FourPlayerMode(playerNames)
}