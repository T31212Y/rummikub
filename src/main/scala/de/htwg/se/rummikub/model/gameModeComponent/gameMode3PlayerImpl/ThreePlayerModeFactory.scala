package de.htwg.se.rummikub.model.gameModeComponent.gameMode3PlayerImpl

import de.htwg.se.rummikub.model.gameModeComponent.{PlayerModeFactoryInterface, GameModeInterface}

class ThreePlayerModeFactory extends PlayerModeFactoryInterface {
  override def supportedPlayerCount: Int = 3
  override def create(playerNames: List[String]): GameModeInterface = new ThreePlayerMode(playerNames)
}