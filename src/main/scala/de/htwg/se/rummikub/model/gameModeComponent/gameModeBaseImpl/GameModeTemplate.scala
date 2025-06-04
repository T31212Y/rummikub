package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface
import de.htwg.se.rummikub.model.playerComponent.{PlayerInterface, PlayerFactoryInterface}
import de.htwg.se.rummikub.model.playingfieldComponent.{PlayingFieldInterface, BoardInterface}
import de.htwg.se.rummikub.model.gameModeComponent.GameModeInterface

abstract class GameModeTemplate(
  playerNames: List[String],
  playerFactory: PlayerFactoryInterface,
  tokenStructureFactory: TokenStructureFactoryInterface
  ) extends GameModeInterface {

  val cntRows: Int = 20
  val cntTokens: Int = 24
  val cntEdgeSpaces: Int = 15

  def runGameSetup(): Option[PlayingFieldInterface] = {
    val players = createPlayers()
    val playingField = createPlayingField(players)
    updatePlayingField(playingField)
  }

  def createPlayers(): List[PlayerInterface] = {
    playerNames.map(name => playerFactory.create(name = name, tokenStructureFactory = tokenStructureFactory))
  }

  def createPlayingField(players: List[PlayerInterface]): Option[PlayingFieldInterface]

  def updatePlayingField(playingField: Option[PlayingFieldInterface]): Option[PlayingFieldInterface]

  def render(playingField: Option[PlayingFieldInterface]): Unit = {
    println(renderPlayingField(playingField))
  }

  def renderPlayingField(playingField: Option[PlayingFieldInterface]): String

  def updateBoardSinglePlayer(player: PlayerInterface, board: BoardInterface): Option[BoardInterface] = {
    if (player.getTokens.size <= cntTokens) {
      board.boardELRP12_1 = board.formatBoardRow(player.getTokens)
      board.boardELRP12_2 = board.formatEmptyBoardRow(board.size(board.boardELRP12_1) - 4)
      board.boardEUD = board.createBoardFrameSingle(player.getTokens)
    } else {
      board.boardELRP12_1 = board.formatBoardRow(player.getTokens.take(cntTokens))
      board.boardELRP12_2 = board.formatBoardRow(player.getTokens.drop(cntTokens))
      board.boardEUD = board.createBoardFrameSingle(player.getTokens.take(cntTokens))
    }
    Some(board)
  }

  def updateBoardMultiPlayer(players: List[PlayerInterface], board: BoardInterface): Option[BoardInterface] = {
    if (players(0).getTokens.size <= cntTokens) {
      board.boardELRP12_1 = board.formatBoardRow(players(0).getTokens)
      board.boardELRP12_2 = board.formatEmptyBoardRow(board.size(board.boardELRP12_1) - 4)
    } else {
      board.boardELRP12_1 = board.formatBoardRow(players(0).getTokens.take(cntTokens))
      board.boardELRP12_2 = board.formatBoardRow(players(0).getTokens.drop(cntTokens))
    }

    if (players(1).getTokens.size <= cntTokens) {
      board.boardELRP34_1 = board.formatBoardRow(players(1).getTokens)
      board.boardELRP34_2 = board.formatEmptyBoardRow(board.size(board.boardELRP34_1) - 4)
    } else {
      board.boardELRP34_1 = board.formatBoardRow(players(1).getTokens.take(cntTokens))
      board.boardELRP34_2 = board.formatBoardRow(players(1).getTokens.drop(cntTokens))
    }

    board.boardEUD = board.createBoardFrameDouble(players(0).getTokens.take(cntTokens), players(1).getTokens.take(cntTokens))
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
