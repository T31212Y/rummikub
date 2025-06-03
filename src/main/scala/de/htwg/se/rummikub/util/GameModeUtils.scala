package de.htwg.se.rummikub.util

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.model.playingfieldComponent.Board

object GameModeUtils {

    val cntRows = 20
    val cntTokens = 24

    def updateBoardSinglePlayer(player: Player, board: Board): Option[Board] = {
        if (player.tokens.size <= cntTokens) {
          board.boardELRP12_1 = board.formatBoardRow(player.tokens)
          board.boardELRP12_2 = board.formatEmptyBoardRow(board.size(board.boardELRP12_1) - 4)
          board.boardEUD = board.createBoardFrameSingle(player.tokens)
        } else {
          board.boardELRP12_1 = board.formatBoardRow(player.tokens.take(cntTokens))
          board.boardELRP12_2 = board.formatBoardRow(player.tokens.drop(cntTokens))
          board.boardEUD = board.createBoardFrameSingle(player.tokens.take(cntTokens))
        }
        Some(board)
    }

    def updateBoardMultiPlayer(players: List[Player], board: Board): Option[Board] = {
        if (players(0).tokens.size <= cntTokens) {
            board.boardELRP12_1 = board.formatBoardRow(players(0).tokens)
            board.boardELRP12_2 = board.formatEmptyBoardRow(board.size(board.boardELRP12_1) - 4)
        } else {
            board.boardELRP12_1 = board.formatBoardRow(players(0).tokens.take(cntTokens))
            board.boardELRP12_2 = board.formatBoardRow(players(0).tokens.drop(cntTokens))
        }
     
        if (players(1).tokens.size <= cntTokens) {
            board.boardELRP34_1 = board.formatBoardRow(players(1).tokens)
            board.boardELRP34_2 = board.formatEmptyBoardRow(board.size(board.boardELRP34_1) - 4)
        } else {
            board.boardELRP34_1 = board.formatBoardRow(players(1).tokens.take(cntTokens))
            board.boardELRP34_2 = board.formatBoardRow(players(1).tokens.drop(cntTokens))
        }

        board.boardEUD = board.createBoardFrameDouble(players(0).tokens.take(cntTokens), players(1).tokens.take(cntTokens))
        Some(board)
    }

    def lineWithPlayerName(char: String, length: Int, player: String): Option[String] = {
        val len = length - player.length - 5
        Some(char.repeat(5) + player + char * len)
    }

    def lineWith2PlayerNames(char: String, length: Int, player1: String, player2: String): Option[String] = {
        val len = length - player1.length - player2.length - 10
        Some(char.repeat(5) + player1 + char * len + player2 + char.repeat(5))
    }

    def cleanRenderOutput(input: String): String = input.replace("x", " ").replace("y", " ")
}