package de.htwg.se.rummikub.model

trait PlayingFieldBuilder {
    def setPlayers(players: List[Player]): PlayingFieldBuilder
    def setBoards(boards: List[Board]): PlayingFieldBuilder
    def setInnerField(innerField: Table): PlayingFieldBuilder
    def build(): PlayingField
}