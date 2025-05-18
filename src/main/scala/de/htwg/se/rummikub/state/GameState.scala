package de.htwg.se.rummikub.state
import de.htwg.se.rummikub.model.{Player, Table}

case class GameState(
  table: Table,
  players: Vector[Player],
  currentPlayerIndex: Int
) {

  def currentPlayer: Player = players(currentPlayerIndex)

  def updateCurrentPlayer(updatedPlayer: Player): GameState =
    copy(players = players.updated(currentPlayerIndex, updatedPlayer))

  def nextTurn(): GameState =
    copy(currentPlayerIndex = (currentPlayerIndex + 1) % players.length)

  def updateTable(newTable: Table): GameState =
    copy(table = newTable)

  def updatePlayerById(id: String, updated: Player): GameState =
    copy(players = players.map(p => if (p.name == id) updated else p))
}
