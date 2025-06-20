package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.{BoardInterface, TokenStackInterface, TableInterface}
import de.htwg.se.rummikub.controller.controllerComponent.GameStateInterface

case class GameState(
  table: TableInterface,
  players: Vector[PlayerInterface],
  boards: Vector[BoardInterface],
  currentPlayerIndex: Int,
  stack: TokenStackInterface,
  finalRoundsLeft: Option[Int] = None
) extends GameStateInterface {

  override def getTable: TableInterface = table

  override def getPlayers: Vector[PlayerInterface] = players

  override def getBoards: Vector[BoardInterface] = boards

  override def getCurrentPlayerIndex: Int = currentPlayerIndex

  override def getFinalRoundsLeft: Option[Int] = finalRoundsLeft

  override def currentPlayer: PlayerInterface = players(currentPlayerIndex)

  override def currentBoard: BoardInterface = boards(currentPlayerIndex)

  override def currentStack: TokenStackInterface = stack

  override def updateCurrentPlayer(updatedPlayer: PlayerInterface): GameState = copy(players = players.updated(currentPlayerIndex, updatedPlayer))

  override def updateCurrentBoard(updatedBoard: BoardInterface): GameState = copy(boards = boards.updated(currentPlayerIndex, updatedBoard))

  override def updatePlayerById(id: String, updated: PlayerInterface): GameState = copy(players = players.map(p => if (p.getName == id) updated else p))

  override def updateBoardByIndex(index: Int, updated: BoardInterface): GameState = copy(boards = boards.updated(index, updated))

  override def updateTable(newTable: TableInterface): GameState = copy(table = newTable)

  override def updateStack(newStack: TokenStackInterface): GameState = copy(stack = newStack)
  
  override def updatePlayerIndex(newIndex: Int): GameState = copy(currentPlayerIndex = newIndex)

  override def updatePlayers(newPlayers: Vector[PlayerInterface]): GameState = copy(players = newPlayers)

  override def nextTurn: GameState = copy(currentPlayerIndex = (currentPlayerIndex + 1) % players.length)

  override def updated(newPlayers: Vector[PlayerInterface], newStack: TokenStackInterface, newFinalRoundsLeft: Option[Int]): GameStateInterface = copy(players = newPlayers, stack = newStack, finalRoundsLeft = newFinalRoundsLeft)
}