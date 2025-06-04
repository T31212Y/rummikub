package de.htwg.se.rummikub.model.gameModeComponent

trait GameModeFactoryInterface {
  def create(mode: Int, playerNames: List[String]): GameModeInterface
}