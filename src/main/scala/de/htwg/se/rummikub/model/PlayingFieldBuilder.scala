package de.htwg.se.rummikub.model
import de.htwg.se.rummikub.model.playingfieldComponent.TokenStackInterface
import playingfieldComponent.playingFieldBaseImpl.Table
import de.htwg.se.rummikub.model.playingfieldComponent.BoardInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingfieldComponent.PlayingFieldInterface

trait PlayingFieldBuilder {
    def setPlayers(players: List[PlayerInterface]): PlayingFieldBuilder
    def setBoards(boards: List[BoardInterface]): PlayingFieldBuilder
    def setInnerField(innerField: Table): PlayingFieldBuilder
    def setStack(stack: TokenStackInterface): PlayingFieldBuilder
    def build(): PlayingFieldInterface
}