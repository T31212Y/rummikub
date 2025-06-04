package de.htwg.se.rummikub.model.gameModeComponent

import de.htwg.se.rummikub.model.playingfieldComponent.PlayingFieldInterface

trait GameModeInterface {
  def runGameSetup: Option[PlayingFieldInterface]
  def render(playingField: Option[PlayingFieldInterface]): Unit
}