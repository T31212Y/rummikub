package de.htwg.se.rummikub.util

import de.htwg.se.rummikub.model.playingfieldComponent.BoardInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

object GameModeUtils {

  val cntRows = 20
  val cntTokens = 24

  def updateBoardSinglePlayer(player: PlayerInterface, board: BoardInterface): BoardInterface = {
    if (player.getTokens.size <= cntTokens) {
      board.boardELRP12_1 = board.formatBoardRow(player.getTokens)
      board.boardELRP12_2 = board.formatEmptyBoardRow(board.size(board.boardELRP12_1) - 4)
      board.boardEUD = board.createBoardFrameSingle(player.getTokens)
    } else {
      board.boardELRP12_1 = board.formatBoardRow(player.getTokens.take(cntTokens))
      board.boardELRP12_2 = board.formatBoardRow(player.getTokens.drop(cntTokens))
      board.boardEUD = board.createBoardFrameSingle(player.getTokens.take(cntTokens))
    }
    board
  }

  def updateBoardMultiPlayer(players: List[PlayerInterface], board: BoardInterface): BoardInterface = {
    if (players.nonEmpty) {
      val p1 = players.head
      if (p1.getTokens.size <= cntTokens) {
        board.boardELRP12_1 = board.formatBoardRow(p1.getTokens)
        board.boardELRP12_2 = board.formatEmptyBoardRow(board.size(board.boardELRP12_1) - 4)
      } else {
        board.boardELRP12_1 = board.formatBoardRow(p1.getTokens.take(cntTokens))
        board.boardELRP12_2 = board.formatBoardRow(p1.getTokens.drop(cntTokens))
      }
    }
    if (players.length > 1) {
      val p2 = players(1)
      if (p2.getTokens.size <= cntTokens) {
        board.boardELRP34_1 = board.formatBoardRow(p2.getTokens)
        board.boardELRP34_2 = board.formatEmptyBoardRow(board.size(board.boardELRP34_1) - 4)
      } else {
        board.boardELRP34_1 = board.formatBoardRow(p2.getTokens.take(cntTokens))
        board.boardELRP34_2 = board.formatBoardRow(p2.getTokens.drop(cntTokens))
      }
      board.boardEUD = board.createBoardFrameDouble(players(0).getTokens.take(cntTokens), p2.getTokens.take(cntTokens))
    }
    board
  }

  def lineWithPlayerName(char: String, length: Int, player: String): String = {
    val len = length - player.length - 5
    char.repeat(5) + player + char * len
  }

  def lineWith2PlayerNames(char: String, length: Int, player1: String, player2: String): String = {
    val len = length - player1.length - player2.length - 10
    char.repeat(5) + player1 + char * len + player2 + char.repeat(5)
  }

  def cleanRenderOutput(input: String): String = input.replace("x", " ").replace("y", " ")
}
