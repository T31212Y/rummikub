package de.htwg.se.rummikub.model

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.{TokenStack, PlayingField}
import de.htwg.se.rummikub.model.playingFieldComponent.BoardInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableInterface

trait PlayingFieldBuilder {
    def setPlayers(players: List[PlayerInterface]): PlayingFieldBuilder
    def setBoards(boards: List[BoardInterface]): PlayingFieldBuilder
    def setInnerField(innerField: TableInterface): PlayingFieldBuilder
    def setStack(stack: TokenStack): PlayingFieldBuilder
    def build(): PlayingField
}