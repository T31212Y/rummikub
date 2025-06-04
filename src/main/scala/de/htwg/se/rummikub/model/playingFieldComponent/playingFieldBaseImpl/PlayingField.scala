package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.BoardInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackInterface
import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface

case class PlayingField(players: List[PlayerInterface], boards: List[BoardInterface], innerField: TableInterface, stack: TokenStackInterface) extends PlayingFieldInterface {
    override def getPlayers: List[PlayerInterface] = players
    override def getBoards: List[BoardInterface] = boards
    override def getInnerField: TableInterface = innerField
    override def getStack: TokenStackInterface = stack

    override def updated(newPlayers: List[PlayerInterface], newBoards: List[BoardInterface], newInnerField: TableInterface): PlayingFieldInterface = {
        copy(players = newPlayers, boards = newBoards, innerField = newInnerField)
    }
}