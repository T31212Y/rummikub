package de.htwg.se.rummikub.model
import playingfieldComponent.PlayingField
import playingfieldComponent.playingFieldBaseImpl.Table

trait PlayingFieldBuilder {
    def setPlayers(players: List[Player]): PlayingFieldBuilder
    def setBoards(boards: List[Board]): PlayingFieldBuilder
    def setInnerField(innerField: Table): PlayingFieldBuilder
    def setStack(stack: TokenStack): PlayingFieldBuilder
    def build(): PlayingField
}