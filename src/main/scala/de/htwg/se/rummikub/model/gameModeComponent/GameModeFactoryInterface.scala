package de.htwg.se.rummikub.model.gameModeComponent

import scala.util.{Try, Success, Failure}

trait GameModeFactoryInterface {
    def createGameMode(amtPlayers: Int, playerNames: List[String]): Try[GameModeTemplate]
}