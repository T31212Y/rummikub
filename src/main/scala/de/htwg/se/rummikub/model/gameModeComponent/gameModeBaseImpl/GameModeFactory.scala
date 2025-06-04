package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import scala.util.{Try, Success, Failure}

import de.htwg.se.rummikub.model.gameModeComponent.{PlayerModeFactoryInterface, GameModeFactoryInterface, GameModeInterface}

class GameModeFactory(factories: List[PlayerModeFactoryInterface]) extends GameModeFactoryInterface {
  override def createGameMode(amtPlayers: Int, playerNames: List[String]): Try[GameModeInterface] = {
    factories.find(_.supportedPlayerCount == amtPlayers) match {
      case Some(factory) => Try(factory.create(playerNames))
      case None => Failure(new IllegalArgumentException(s"No factory found for $amtPlayers players"))
    }
  }
}