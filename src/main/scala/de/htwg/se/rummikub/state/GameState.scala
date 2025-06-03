package de.htwg.se.rummikub.state

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.model.playingfieldComponent.BoardInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingfieldComponent.TokenStackInterface
import de.htwg.se.rummikub.model.playingfieldComponent.BoardInterface
import de.htwg.se.rummikub.model.playingfieldComponent.TokenStackInterface
import de.htwg.se.rummikub.model.playingfieldComponent.TableInterface

case class GameState(
  table: TableInterface,
  players: Vector[PlayerInterface],
  boards: Vector[BoardInterface],
  currentPlayerIndex: Int,
  stack: TokenStackInterface
) {

  def currentPlayer: PlayerInterface = players(currentPlayerIndex)

  def currentBoard: BoardInterface = boards(currentPlayerIndex)

  def currentStack: TokenStackInterface = stack

  def updateCurrentPlayer(updatedPlayer: PlayerInterface): GameState =
    copy(players = players.updated(currentPlayerIndex, updatedPlayer))

  def updateCurrentBoard(updatedBoard: BoardInterface): GameState =
    copy(boards = boards.updated(currentPlayerIndex, updatedBoard))

  def updatePlayerById(id: String, updated: PlayerInterface): GameState =
    copy(players = players.map(p => if (p.name == id) updated else p))

  def updateBoardByIndex(index: Int, updated: BoardInterface): GameState =
    copy(boards = boards.updated(index, updated))

  def updateTable(newTable: TableInterface): GameState =
    copy(table = newTable)

  def updateStack(newStack: TokenStackInterface): GameState =
    copy(stack = newStack)
  
  def updatePlayerIndex(newIndex: Int): GameState =
    copy(currentPlayerIndex = newIndex)

  def updatePlayers(newPlayers: Vector[PlayerInterface]): GameState = {
    copy(players = newPlayers)
  }

  def nextTurn(): GameState =
    copy(currentPlayerIndex = (currentPlayerIndex + 1) % players.length)
}