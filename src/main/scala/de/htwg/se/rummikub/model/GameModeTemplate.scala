package de.htwg.se.rummikub.model

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player

import de.htwg.se.rummikub.model.playingFieldComponent.BoardInterface
import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface
abstract class GameModeTemplate(playerNames: List[String]) {

    val cntRows: Int = 20
    val cntTokens: Int = 24
    val cntEdgeSpaces: Int = 15

    def runGameSetup(): Option[PlayingFieldInterface] = {
        val players = createPlayers()
        val playingField = createPlayingField(players)
        val updatedPlayingField = updatePlayingField(playingField)
        updatedPlayingField
    }

    def createPlayers(): List[PlayerInterface] = {
        playerNames.map(name => Player(name))
    }

    def createPlayingField(players: List[PlayerInterface]): Option[PlayingFieldInterface]
    def updatePlayingField(playingField: Option[PlayingFieldInterface]): Option[PlayingFieldInterface]

    def render(playingField: Option[PlayingFieldInterface]): Unit = {
        println(renderPlayingField(playingField))
    }

    def renderPlayingField(playingField: Option[PlayingFieldInterface]): String

    def updateBoardSinglePlayer(player: PlayerInterface, board: BoardInterface): Option[BoardInterface] = {
        if (player.getTokens.size <= cntTokens) {
          board.setBoardELRP12_1(board.formatBoardRow(player.getTokens))
          board.setBoardELRP12_2(board.formatEmptyBoardRow(board.size(board.getBoardELRP12_1) - 4))
          board.setBoardEUD(board.createBoardFrameSingle(player.getTokens))
        } else {
          board.setBoardELRP12_1(board.formatBoardRow(player.getTokens.take(cntTokens)))
          board.setBoardELRP12_2(board.formatBoardRow(player.getTokens.drop(cntTokens)))
          board.setBoardEUD(board.createBoardFrameSingle(player.getTokens.take(cntTokens)))
        }
        Some(board)
    }

    def updateBoardMultiPlayer(players: List[PlayerInterface], board: BoardInterface): Option[BoardInterface] = {
        if (players(0).getTokens.size <= cntTokens) {
            board.setBoardELRP12_1(board.formatBoardRow(players(0).getTokens))
            board.setBoardELRP12_2(board.formatEmptyBoardRow(board.size(board.getBoardELRP12_1) - 4))
        } else {
            board.setBoardELRP12_1(board.formatBoardRow(players(0).getTokens.take(cntTokens)))
            board.setBoardELRP12_2(board.formatBoardRow(players(0).getTokens.drop(cntTokens)))
        }
     
        if (players(1).getTokens.size <= cntTokens) {
            board.setBoardELRP34_1(board.formatBoardRow(players(1).getTokens))
            board.setBoardELRP34_2(board.formatEmptyBoardRow(board.size(board.getBoardELRP34_1) - 4))
        } else {
            board.setBoardELRP34_1(board.formatBoardRow(players(1).getTokens.take(cntTokens)))
            board.setBoardELRP34_2(board.formatBoardRow(players(1).getTokens.drop(cntTokens)))
        }

        board.setBoardEUD(board.createBoardFrameDouble(players(0).getTokens.take(cntTokens), players(1).getTokens.take(cntTokens)))
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
}