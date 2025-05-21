package de.htwg.se.rummikub.state

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.util.Command

case class GameState(
    playingField: PlayingField,
    players: Vector[Player],
    currentPlayerIndex: Int,
    round: Int,
    firstMoveTokens: List[Token] = Nil,
    commandHistory: List[Command] = Nil,
    hasPlayed: Boolean = false
) {
  def currentPlayer: Player = players(currentPlayerIndex)

  def updatePlayer(index: Int, player: Player): GameState =
    copy(players = players.updated(index, player))

  def updateCurrentPlayer(player: Player): GameState =
    updatePlayer(currentPlayerIndex, player)

  def nextPlayer: GameState =
    copy(currentPlayerIndex = (currentPlayerIndex + 1) % players.size)

  def withPlayingField(newField: PlayingField): GameState =
    copy(playingField = newField)

  def withCommandHistory(newHistory: List[Command]): GameState =
    copy(commandHistory = newHistory)

  def resetFirstMove: GameState =
    copy(firstMoveTokens = Nil)
}