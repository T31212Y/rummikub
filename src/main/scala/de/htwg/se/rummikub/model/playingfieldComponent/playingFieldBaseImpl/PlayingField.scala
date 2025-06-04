package de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingfieldComponent._
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

case class PlayingField(
  players: List[PlayerInterface],
  boards: List[BoardInterface],
  innerField: TableInterface,
  stack: TokenStackInterface
) extends PlayingFieldInterface {

  override def getPlayers: List[PlayerInterface] = players
  override def getBoards: List[BoardInterface] = boards
  override def getInnerField: TableInterface = innerField
  override def getStack: TokenStackInterface = stack

  override def addPlayer(player: PlayerInterface): PlayingFieldInterface =
    copy(players = players :+ player)

  override def removePlayer(player: PlayerInterface): PlayingFieldInterface =
    copy(players = players.filterNot(_ == player))

  override def addBoard(board: BoardInterface): PlayingFieldInterface =
    copy(boards = boards :+ board)

  override def removeBoard(board: BoardInterface): PlayingFieldInterface =
    copy(boards = boards.filterNot(_ == board))

  override def setInnerField(table: TableInterface): PlayingFieldInterface =
    copy(innerField = table)

  override def setStack(stack: TokenStackInterface): PlayingFieldInterface =
    copy(stack = stack)
}