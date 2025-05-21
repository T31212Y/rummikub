package de.htwg.se.rummikub.model

sealed trait GameModeStrategy {
  def createPlayers(playerNames: List[String]): List[Player]
  def createPlayingField(players: List[Player]): Option[PlayingField]
  def updatePlayingField(field: Option[PlayingField]): Option[PlayingField]
  def renderPlayingField(field: Option[PlayingField]): String
}