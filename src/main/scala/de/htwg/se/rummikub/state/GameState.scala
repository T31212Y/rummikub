package de.htwg.se.rummikub.state

import de.htwg.se.rummikub.model._

case class GameState(
  table: Table,
  players: Vector[Player],
  boards: Vector[Board],
  currentPlayerIndex: Int,
  stack: TokenStack
) {

  def currentPlayer: Player = players(currentPlayerIndex)

  def currentBoard: Board = boards(currentPlayerIndex)

  def currentStack: TokenStack = stack

  def updateCurrentPlayer(updatedPlayer: Player): GameState =
    copy(players = players.updated(currentPlayerIndex, updatedPlayer))

  def updateCurrentBoard(updatedBoard: Board): GameState =
    copy(boards = boards.updated(currentPlayerIndex, updatedBoard))

  def updatePlayerById(id: String, updated: Player): GameState =
    copy(players = players.map(p => if (p.name == id) updated else p))

  def updateBoardByIndex(index: Int, updated: Board): GameState =
    copy(boards = boards.updated(index, updated))

  def updateTable(newTable: Table): GameState =
    copy(table = newTable)

  def nextTurn(): GameState =
    copy(currentPlayerIndex = (currentPlayerIndex + 1) % players.length)
}