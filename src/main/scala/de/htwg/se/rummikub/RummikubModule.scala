package de.htwg.se.rummikub

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.Controller

import de.htwg.se.rummikub.model.gameModeComponent.GameModeFactoryInterface
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory

import de.htwg.se.rummikub.model.builderComponent.{PlayingFieldBuilderInterface, FieldDirectorInterface}
import de.htwg.se.rummikub.model.builderComponent.builderBaseImpl.{StandardPlayingFieldBuilder, TwoPlayerFieldDirector, ThreePlayerFieldDirector, FourPlayerFieldDirector}

import de.htwg.se.rummikub.model.tokenComponent.TokenFactoryInterface
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.StandardTokenFactory

import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.StandardTokenStructureFactory

import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.StandardTokenStackFactory

import de.htwg.se.rummikub.model.playingFieldComponent.TableFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.StandardTableFactory

import de.htwg.se.rummikub.model.playingFieldComponent.BoardFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.StandardBoardFactory

import de.htwg.se.rummikub.model.playerComponent.PlayerFactoryInterface
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.StandardPlayerFactory

class RummikubModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ControllerInterface].to[Controller]
    bind[GameModeFactoryInterface].to[GameModeFactory]

    bind[PlayingFieldBuilderInterface].to[StandardPlayingFieldBuilder]
    bind[FieldDirectorInterface].annotatedWithName("TwoPlayer").to[TwoPlayerFieldDirector]
    bind[FieldDirectorInterface].annotatedWithName("ThreePlayer").to[ThreePlayerFieldDirector]
    bind[FieldDirectorInterface].annotatedWithName("FourPlayer").to[FourPlayerFieldDirector]

    bind[TokenFactoryInterface].to[StandardTokenFactory]
    bind[TokenStructureFactoryInterface].to[StandardTokenStructureFactory]
    bind[TokenStackFactoryInterface].to[StandardTokenStackFactory]
    bind[TableFactoryInterface].to[StandardTableFactory]
    bind[BoardFactoryInterface].to[StandardBoardFactory]
    bind[PlayerFactoryInterface].to[StandardPlayerFactory]
  }
}