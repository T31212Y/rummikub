package de.htwg.se.rummikub.model.gameModeComponent

import de.htwg.se.rummikub.model.playingfieldComponent.PlayingFieldInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

trait GameModeInterface {
  def runGameSetup: Option[PlayingFieldInterface]
  def createPlayingField(players: List[PlayerInterface]): Option[PlayingFieldInterface]
  def updatePlayingField(playingField: Option[PlayingFieldInterface]): Option[PlayingFieldInterface]
  def renderPlayingField(playingField: Option[PlayingFieldInterface]): String
}