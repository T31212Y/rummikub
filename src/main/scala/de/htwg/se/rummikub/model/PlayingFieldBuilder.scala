package de.htwg.se.rummikub.model

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

import de.htwg.se.rummikub.model.playingFieldComponent.BoardInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackInterface
import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface

trait PlayingFieldBuilder {
    def setPlayers(players: List[PlayerInterface]): PlayingFieldBuilder
    def setBoards(boards: List[BoardInterface]): PlayingFieldBuilder
    def setInnerField(innerField: TableInterface): PlayingFieldBuilder
    def setStack(stack: TokenStackInterface): PlayingFieldBuilder
    def build(): PlayingFieldInterface
}