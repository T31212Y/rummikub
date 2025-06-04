package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import scala.util.{Try, Success, Failure}

import de.htwg.se.rummikub.model.gameModeComponent.{PlayerModeFactoryInterface, GameModeFactoryInterface, GameModeInterface}

import de.htwg.se.rummikub.model.gameModeComponent.gameMode2PlayerImpl._
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.StandardPlayerFactory
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.StandartTokenStrutureFactory

class GameModeFactory(factories: List[PlayerModeFactoryInterface]) extends GameModeFactoryInterface {
  /*override def createGameMode(amtPlayers: Int, playerNames: List[String]): Try[GameModeInterface] = {
    factories.find(_.supportedPlayerCount == amtPlayers) match {
      case Some(factory) => Try(factory.create(playerNames))
      case None => Failure(new IllegalArgumentException(s"No factory found for $amtPlayers players"))
    }
  }*/
  override def createGameMode(amtPlayers: Int, playerNames: List[String]): Try[GameModeInterface] = { amtPlayers match {
    case 2 => Success(TwoPlayerMode(playerNames, new StandardPlayerFactory, new StandartTokenStrutureFactory, new StandardBuilderFactory, new FieldDirectorFactory, new StandardPlayingFieldFactory, new StandardTableFactory))
    //case 3 => Success(ThreePlayerMode(playerNames))
    //case 4 => Success(FourPlayerMode(playerNames))
    case _ => Failure(new IllegalArgumentException("Invalid number of players"))
    }
  }
}