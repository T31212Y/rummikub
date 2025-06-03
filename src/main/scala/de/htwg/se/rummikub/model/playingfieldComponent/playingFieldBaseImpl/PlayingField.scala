package de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingfieldComponent._
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

case class PlayingField(
  players: List[PlayerInterface],
  boards: List[BoardInterface],
  innerField: TableInterface,
  stack: TokenStackInterface
) extends PlayingFieldInterface {

  def getPlayers: List[PlayerInterface] = players
  def getBoards: List[BoardInterface] = boards
  def getInnerField: TableInterface = innerField
  def getStack: TokenStackInterface = stack

  def addPlayer(player: PlayerInterface): PlayingFieldInterface =
    copy(players = players :+ player)

  def removePlayer(player: PlayerInterface): PlayingFieldInterface =
    copy(players = players.filterNot(_ == player))

  def addBoard(board: BoardInterface): PlayingFieldInterface =
    copy(boards = boards :+ board)

  def removeBoard(board: BoardInterface): PlayingFieldInterface =
    copy(boards = boards.filterNot(_ == board))

  def setInnerField(table: TableInterface): PlayingFieldInterface =
    copy(innerField = table)

  def setStack(stack: TokenStackInterface): PlayingFieldInterface =
    copy(stack = stack)
}
