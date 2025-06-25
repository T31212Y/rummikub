package de.htwg.se.rummikub.model.gameModeComponent

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.{BoardInterface, PlayingFieldInterface}
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

trait GameModeTemplate {
    val tokenStructureFactory: TokenStructureFactoryInterface
    val cntRows: Int = 20
    val cntTokens: Int = 24
    val cntEdgeSpaces: Int = 15

    def runGameSetup: Option[PlayingFieldInterface] = {
        val players = createPlayers
        val playingField = createPlayingField(players)
        val updatedPlayingField = updatePlayingField(playingField)
        updatedPlayingField
    }

    def createPlayers: List[PlayerInterface]

    def createPlayingField(players: List[PlayerInterface]): Option[PlayingFieldInterface]
    def updatePlayingField(playingField: Option[PlayingFieldInterface]): Option[PlayingFieldInterface]
    def renderPlayingField(playingField: Option[PlayingFieldInterface]): String

    def render(playingField: Option[PlayingFieldInterface]): Unit = {
        println(renderPlayingField(playingField))
    }

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

    def isSortedAndContinuous(tokens: List[TokenInterface]): Boolean = {
        val numbers = tokens.collect { case t if t.isNumToken => t.getNumber.get }
        if (numbers.isEmpty) return false
        val sorted = numbers.sorted
        sorted == numbers && sorted.sliding(2).forall {
            case List(a, b) => b == a + 1
            case _ => true
        }
    }

    def isSortedAndContinuousWithJoker(tokens: List[TokenInterface]): Boolean = {
        val (jokers, nonJokers) = tokens.partition(_.isJoker)
        val numbers = nonJokers.flatMap(_.getNumber).distinct.sorted

        val totalLength = tokens.size

        (1 to 13).exists { start =>
            val sequence = LazyList.iterate(start)(_ % 13 + 1).take(totalLength).toList
            val missing = sequence.diff(numbers)
            missing.size <= jokers.size
        }
    }



    def isValidTable(
        table: List[List[TokenInterface]],
        tokenStructureFactory: TokenStructureFactoryInterface
    ): Boolean = {
        table.filter(_.nonEmpty).forall { row =>
            val group = tokenStructureFactory.createGroup(row)
            val sequence = tokenStructureFactory.createRow(row)
            group.isValid || (sequence.isValid && isSortedAndContinuousWithJoker(row))
        }
    }
}