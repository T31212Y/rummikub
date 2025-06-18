package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import scala.util.{Try, Success, Failure}
import de.htwg.se.rummikub.model.gameModeComponent.{GameModeTemplate, GameModeFactoryInterface}

import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.BoardFactoryInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerFactoryInterface

import com.google.inject.Inject

class GameModeFactory @Inject() (tokenStackFactory: TokenStackFactoryInterface, tableFactory: TableFactoryInterface, boardFactory: BoardFactoryInterface, playerFactory: PlayerFactoryInterface) extends GameModeFactoryInterface {
  override def createGameMode(amtPlayers: Int, playerNames: List[String]): Try[GameModeTemplate] = amtPlayers match {
    case 2 => Success(TwoPlayerMode(playerNames, tokenStackFactory, tableFactory, boardFactory, playerFactory))
    case 3 => Success(ThreePlayerMode(playerNames, tokenStackFactory, tableFactory, boardFactory, playerFactory))
    case 4 => Success(FourPlayerMode(playerNames, tokenStackFactory, tableFactory, boardFactory, playerFactory))
    case _ => Failure(new IllegalArgumentException("Invalid number of players"))
  }
}