package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import scala.util.{Try, Success, Failure}

import de.htwg.se.rummikub.model.gameModeComponent.{GameModeTemplate, GameModeFactoryInterface}
import de.htwg.se.rummikub.model.playingFieldComponent.{TokenStackFactoryInterface, TableFactoryInterface, BoardFactoryInterface}
import de.htwg.se.rummikub.model.playerComponent.PlayerFactoryInterface
import de.htwg.se.rummikub.model.builderComponent.{PlayingFieldBuilderInterface, FieldDirectorInterface}

import com.google.inject.Inject
import com.google.inject.name.Named

class GameModeFactory @Inject() (tokenStackFactory: TokenStackFactoryInterface, 
tableFactory: TableFactoryInterface, 
boardFactory: BoardFactoryInterface, 
playerFactory: PlayerFactoryInterface,
playingFieldBuilder: PlayingFieldBuilderInterface,
@Named("TwoPlayer") twoPlayerDirector: FieldDirectorInterface,
@Named("ThreePlayer") threePlayerDirector: FieldDirectorInterface,
@Named("FourPlayer") fourPlayerDirector: FieldDirectorInterface
) extends GameModeFactoryInterface {
  override def createGameMode(amtPlayers: Int, playerNames: List[String]): Try[GameModeTemplate] = amtPlayers match {
    case 2 => Success(TwoPlayerMode(playerNames, tokenStackFactory, tableFactory, boardFactory, playerFactory, playingFieldBuilder, twoPlayerDirector))
    case 3 => Success(ThreePlayerMode(playerNames, tokenStackFactory, tableFactory, boardFactory, playerFactory, playingFieldBuilder, threePlayerDirector))
    case 4 => Success(FourPlayerMode(playerNames, tokenStackFactory, tableFactory, boardFactory, playerFactory, playingFieldBuilder, fourPlayerDirector))
    case _ => Failure(new IllegalArgumentException("Invalid number of players"))
  }
}