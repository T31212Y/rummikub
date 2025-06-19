package de.htwg.se.rummikub

import de.htwg.se.rummikub.model.gameModeComponent.GameModeFactoryInterface
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.Controller

import de.htwg.se.rummikub.model.tokenComponent.TokenFactoryInterface
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.StandardTokenFactory

import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.StandardTokenStructureFactory

import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.StandardTokenStackFactory

import de.htwg.se.rummikub.model.playingFieldComponent.TableFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.StandardTableFactory

import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.builderComponent.builderBaseImpl.StandardPlayingFieldBuilder

import de.htwg.se.rummikub.model.playerComponent.PlayerFactoryInterface
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.StandardPlayerFactory

import de.htwg.se.rummikub.model.playingFieldComponent.BoardFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.StandardBoardFactory

object RummikubDependencyModule {
    given tokenFactory: TokenFactoryInterface = new StandardTokenFactory
    given tokenStackFactory: TokenStackFactoryInterface = new StandardTokenStackFactory
    given tokenStructureFactory: TokenStructureFactoryInterface = new StandardTokenStructureFactory
    given tableFactory: TableFactoryInterface = new StandardTableFactory
    given boardFactory: BoardFactoryInterface = new StandardBoardFactory
    given playerFactory: PlayerFactoryInterface = new StandardPlayerFactory
    given playingFieldBuilder: PlayingFieldBuilderInterface = new StandardPlayingFieldBuilder
    given gameModeFactory: GameModeFactoryInterface = new GameModeFactory
    given controller: ControllerInterface = new Controller
}