package de.htwg.se.rummikub

import de.htwg.se.rummikub.model.gameModeComponent.GameModeFactoryInterface
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.Controller

import de.htwg.se.rummikub.model.tokenComponent.TokenFactoryInterface
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.StandardTokenFactory

import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.StandardTokenStructureFactory

object RummikubDependencyModule {
    given gameModeFactory: GameModeFactoryInterface = new GameModeFactory
    given tokenFactory: TokenFactoryInterface = new StandardTokenFactory
    given tokenStructureFactory: TokenStructureFactoryInterface = new StandardTokenStructureFactory
    given controller: ControllerInterface = new Controller
}