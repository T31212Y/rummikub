package de.htwg.se.rummikub

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

import de.htwg.se.rummikub.controller.controllerComponent.{ControllerInterface, GameStateInterface}
import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.{Controller, GameState}

import de.htwg.se.rummikub.model.builderComponent.{PlayingFieldBuilderInterface, FieldDirectorInterface}
import de.htwg.se.rummikub.model.builderComponent.builderBaseImpl.{StandardPlayingFieldBuilder, TwoPlayerFieldDirector, ThreePlayerFieldDirector, FourPlayerFieldDirector}

import de.htwg.se.rummikub.model.gameModeComponent.{GameModeFactoryInterface, GameModeTemplate}
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.{GameModeFactory, TwoPlayerMode, ThreePlayerMode, FourPlayerMode}

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player

import de.htwg.se.rummikub.model.playingFieldComponent.{PlayingFieldInterface, BoardInterface, TableInterface, TokenStackInterface}
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.{PlayingField, Board, Table, TokenStack}

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, TokenFactoryInterface}
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.{Joker, NumToken, StandardTokenFactory}

import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.{Row, Group}

class RummikubModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ControllerInterface].to[Controller]
    /*bind[GameStateInterface].to[GameState]

    bind[PlayingFieldBuilderInterface].to[StandardPlayingFieldBuilder]
    bind[FieldDirectorInterface].annotatedWithName("TwoPlayer").to[TwoPlayerFieldDirector]
    bind[FieldDirectorInterface].annotatedWithName("ThreePlayer").to[ThreePlayerFieldDirector]
    bind[FieldDirectorInterface].annotatedWithName("FourPlayer").to[FourPlayerFieldDirector]*/

    bind[GameModeFactoryInterface].to[GameModeFactory]
    /*bind[GameModeTemplate].annotatedWithName("TwoPlayerMode").to[TwoPlayerMode]
    bind[GameModeTemplate].annotatedWithName("ThreePlayerMode").to[ThreePlayerMode]
    bind[GameModeTemplate].annotatedWithName("FourPlayerMode").to[FourPlayerMode]

    bind[PlayerInterface].to[Player]

    bind[PlayingFieldInterface].to[PlayingField]
    bind[BoardInterface].to[Board]
    bind[TableInterface].to[Table]
    bind[TokenStackInterface].to[TokenStack]

    bind[TokenInterface].annotatedWithName("NumToken").to[NumToken]
    bind[TokenInterface].annotatedWithName("Joker").to[Joker]
    bind[TokenFactoryInterface].to[StandardTokenFactory]

    bind[TokenStructureInterface].annotatedWithName("Row").to[Row]
    bind[TokenStructureInterface].annotatedWithName("Group").to[Group]*/
  }
}