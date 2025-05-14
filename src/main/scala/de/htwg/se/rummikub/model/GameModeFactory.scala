package de.htwg.se.rummikub.model

object GameModeFactory {
  def createGameMode(amtPlayers: Int, playerNames: List[String]): GameModeTemplate = amtPlayers match {
    case 2 => TwoPlayerMode(playerNames)
    case 3 => ThreePlayerMode(playerNames)
    //case 4 => FourPlayerMode(playerNames)
  }
}