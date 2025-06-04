package de.htwg.se.rummikub.model

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

trait PlayingFieldBuilder {
    def setPlayers(players: List[PlayerInterface]): PlayingFieldBuilder
    def setBoards(boards: List[Board]): PlayingFieldBuilder
    def setInnerField(innerField: Table): PlayingFieldBuilder
    def setStack(stack: TokenStack): PlayingFieldBuilder
    def build(): PlayingField
}