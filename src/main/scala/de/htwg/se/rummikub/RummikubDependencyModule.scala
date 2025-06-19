package de.htwg.se.rummikub

import de.htwg.se.rummikub.model.gameModeComponent.GameModeFactoryInterface
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.Controller

object RummikubDependencyModule {
    given gameModeFactory: GameModeFactoryInterface = new GameModeFactory
    given controller: ControllerInterface = new Controller
}