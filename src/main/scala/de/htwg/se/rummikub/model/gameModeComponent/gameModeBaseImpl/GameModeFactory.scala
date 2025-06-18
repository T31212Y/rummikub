package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import scala.util.{Try, Success, Failure}
import de.htwg.se.rummikub.model.gameModeComponent.{GameModeTemplate, GameModeFactoryInterface}

import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableFactoryInterface

import com.google.inject.Inject

class GameModeFactory @Inject() (tokenStackFactory: TokenStackFactoryInterface, tableFactory: TableFactoryInterface) extends GameModeFactoryInterface {
  override def createGameMode(amtPlayers: Int, playerNames: List[String]): Try[GameModeTemplate] = amtPlayers match {
    case 2 => Success(TwoPlayerMode(playerNames, tokenStackFactory, tableFactory))
    case 3 => Success(ThreePlayerMode(playerNames, tokenStackFactory, tableFactory))
    case 4 => Success(FourPlayerMode(playerNames, tokenStackFactory, tableFactory))
    case _ => Failure(new IllegalArgumentException("Invalid number of players"))
  }
}