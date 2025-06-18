package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import scala.util.{Try, Success, Failure}
import de.htwg.se.rummikub.model.gameModeComponent.{GameModeTemplate, GameModeFactoryInterface}

import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface

import com.google.inject.Inject

class GameModeFactory @Inject() (tokenStackFactory: TokenStackFactoryInterface) extends GameModeFactoryInterface {
  override def createGameMode(amtPlayers: Int, playerNames: List[String]): Try[GameModeTemplate] = amtPlayers match {
    case 2 => Success(TwoPlayerMode(playerNames, tokenStackFactory))
    case 3 => Success(ThreePlayerMode(playerNames, tokenStackFactory))
    case 4 => Success(FourPlayerMode(playerNames, tokenStackFactory))
    case _ => Failure(new IllegalArgumentException("Invalid number of players"))
  }
}