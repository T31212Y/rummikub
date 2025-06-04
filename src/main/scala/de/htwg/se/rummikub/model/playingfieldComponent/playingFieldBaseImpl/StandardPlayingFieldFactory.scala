package de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingfieldComponent.{PlayingFieldFactoryInterface, PlayingFieldInterface}
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

class StandardPlayingFieldFactory extends PlayingFieldFactoryInterface {
  override def create(
    players: List[PlayerInterface],
    boards: List[Board],
    innerField: Table,
    stack: TokenStack
  ): PlayingFieldInterface = {
    PlayingField(players, boards, innerField, stack)
  }
}