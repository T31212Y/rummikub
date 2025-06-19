package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import scala.util.{Try, Success, Failure}

import de.htwg.se.rummikub.model.gameModeComponent.{GameModeTemplate, GameModeFactoryInterface}
import de.htwg.se.rummikub.model.playingFieldComponent.{TokenStackFactoryInterface, TableFactoryInterface, BoardFactoryInterface}
import de.htwg.se.rummikub.model.playerComponent.PlayerFactoryInterface
import de.htwg.se.rummikub.model.builderComponent.{PlayingFieldBuilderInterface, FieldDirectorInterface}

import de.htwg.se.rummikub.RummikubDependencyModule.{TwoPlayerTag, ThreePlayerTag, FourPlayerTag}

class GameModeFactory (using tokenStackFactory: TokenStackFactoryInterface,
                             tableFactory: TableFactoryInterface,
                             boardFactory: BoardFactoryInterface,
                             playerFactory: PlayerFactoryInterface,
                             playingFieldBuilder: PlayingFieldBuilderInterface) extends GameModeFactoryInterface {
  override def createGameMode(amtPlayers: Int, playerNames: List[String]): Try[GameModeTemplate] = amtPlayers match {
    case 2 => {
      val director = summon[FieldDirectorInterface & TwoPlayerTag]
      Success(TwoPlayerMode(playerNames)(using tokenStackFactory, tableFactory, boardFactory, playerFactory, playingFieldBuilder, director))
    }
    case 3 => {
      val director = summon[FieldDirectorInterface & ThreePlayerTag]
      Success(ThreePlayerMode(playerNames)(using tokenStackFactory, tableFactory, boardFactory, playerFactory, playingFieldBuilder, director))
    }
    case 4 => {
      val director = summon[FieldDirectorInterface & FourPlayerTag]
      Success(FourPlayerMode(playerNames)(using tokenStackFactory, tableFactory, boardFactory, playerFactory, playingFieldBuilder, director))
    }
    case _ => Failure(new IllegalArgumentException("Invalid number of players"))
  }
}