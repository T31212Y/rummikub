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
        val values = tokens.map {
            case t if t.isNumToken => Some(t.getNumber.get)
            case t if t.isJoker => None
        }
        def nextValue(n: Int): Int = if (n == 13) 1 else n + 1

        def helper(vals: List[Option[Int]], prev: Option[Int]): Boolean = vals match {
            case Nil => true
            case Some(n) :: rest =>
                prev match {
                    case Some(p) if n != nextValue(p) => false
                    case _ => helper(rest, Some(n))
                }
            case None :: rest =>
                prev match {
                    case Some(p) => helper(rest, Some(nextValue(p)))
                    case None => helper(rest, None)
                }
        }
        helper(values, values.head)
    }

    def isValidTable(table: List[List[TokenInterface]]): Boolean = {
        table.filter(_.nonEmpty).forall { row =>
            val group = tokenStructureFactory.createGroup(row)
            val sequence = tokenStructureFactory.createRow(row)
            group.isValid || (sequence.isValid && isSortedAndContinuousWithJoker(row))
        }
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
}