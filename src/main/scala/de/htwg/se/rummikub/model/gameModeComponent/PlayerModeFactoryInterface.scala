package de.htwg.se.rummikub.model.gameModeComponent

trait PlayerModeFactoryInterface {
  def supportedPlayerCount: Int
  def create(playerNames: List[String]): GameModeInterface
}
