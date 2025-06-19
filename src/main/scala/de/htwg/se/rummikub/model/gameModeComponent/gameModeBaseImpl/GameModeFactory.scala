package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import scala.util.{Try, Success, Failure}
import de.htwg.se.rummikub.model.gameModeComponent.{GameModeTemplate, GameModeFactoryInterface}

import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerFactoryInterface

class GameModeFactory (using tokenStackFactory: TokenStackFactoryInterface, playerFactory: PlayerFactoryInterface) extends GameModeFactoryInterface {
  override def createGameMode(amtPlayers: Int, playerNames: List[String]): Try[GameModeTemplate] = amtPlayers match {
    case 2 => Success(TwoPlayerMode(playerNames))
    case 3 => Success(ThreePlayerMode(playerNames))
    case 4 => Success(FourPlayerMode(playerNames))
    case _ => Failure(new IllegalArgumentException("Invalid number of players"))
  }
}