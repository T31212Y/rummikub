package de.htwg.se.rummikub.model.gameModeComponent.gameMode2PlayerImpl

import de.htwg.se.rummikub.model.gameModeComponent.{PlayerModeFactoryInterface, GameModeInterface}

class TwoPlayerModeFactory extends PlayerModeFactoryInterface {
  override def supportedPlayerCount: Int = 2
  override def create(playerNames: List[String]): GameModeInterface = new TwoPlayerMode(playerNames)
}