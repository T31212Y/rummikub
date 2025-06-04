package de.htwg.se.rummikub.model.builderComponent

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingfieldComponent.PlayingFieldInterface

trait FieldDirectorInterface {
  def construct(players: List[PlayerInterface]): PlayingFieldInterface
}