package de.htwg.se.rummikub.model.playingFieldComponent

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

trait PlayingFieldInterface {
    def getPlayers: List[PlayerInterface]
    def getBoards: List[BoardInterface]
    def getInnerField: TableInterface
    def getStack: TokenStackInterface

    def updated(newPlayers: List[PlayerInterface], newBoards: List[BoardInterface], newInnerField: TableInterface): PlayingFieldInterface
}