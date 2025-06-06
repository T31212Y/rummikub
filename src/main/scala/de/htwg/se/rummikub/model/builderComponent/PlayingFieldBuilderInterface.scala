package de.htwg.se.rummikub.model.builderComponent

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

import de.htwg.se.rummikub.model.playingFieldComponent.BoardInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackInterface
import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface

trait PlayingFieldBuilderInterface {
    def setPlayers(players: List[PlayerInterface]): PlayingFieldBuilderInterface
    def setBoards(boards: List[BoardInterface]): PlayingFieldBuilderInterface
    def setInnerField(innerField: TableInterface): PlayingFieldBuilderInterface
    def setStack(stack: TokenStackInterface): PlayingFieldBuilderInterface
    def build(): PlayingFieldInterface
}