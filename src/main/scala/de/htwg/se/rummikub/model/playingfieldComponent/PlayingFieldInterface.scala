package de.htwg.se.rummikub.model.playingfieldComponent

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

trait BoardInterface {
  def toStringRepresentation: String

  def boardELRP12_1: String
  def boardELRP12_2: String
  def boardELRP34_1: String
  def boardELRP34_2: String
  def boardEUD: String

  def formatBoardRow(tokens: List[TokenStructureInterface]): String
  def formatEmptyBoardRow(length: Int): String
  def createBoardFrameSingle(tokens: List[TokenStructureInterface]): String
  def createBoardFrameDouble(tokens1: List[TokenStructureInterface], tokens2: List[TokenStructureInterface]): String

  def boardELRP12_1_=(value: String): Unit
  def boardELRP12_2_=(value: String): Unit
  def boardELRP34_1_=(value: String): Unit
  def boardELRP34_2_=(value: String): Unit
  def boardEUD_=(value: String): Unit

  def size(row: String): Int

  def wrapBoardRowSingle(board: String): String
  def wrapBoardRowDouble(board1: String, board2: String): String
}

trait TableInterface {
  def add(row: List[TokenStructureInterface]): TableInterface
  def remove(tokens: List[TokenStructureInterface]): TableInterface
  def getRow(index: Int): Option[List[TokenStructureInterface]]
  def updateRow(index: Int, newTokens: List[TokenStructureInterface]): TableInterface
  def getTokens: List[List[TokenStructureInterface]]
}

trait TokenStackInterface {
  def drawToken(): (TokenStructureInterface, TokenStackInterface)
  def drawMultipleTokens(n: Int): (List[TokenStructureInterface], TokenStackInterface)
  def removeToken(token: TokenStructureInterface): TokenStackInterface
  def isEmpty: Boolean
  def size: Int
}

trait PlayingFieldInterface {
  def getPlayers: List[PlayerInterface]
  def getBoards: List[BoardInterface]
  def getInnerField: TableInterface
  def getStack: TokenStackInterface

  def addPlayer(player: PlayerInterface): PlayingFieldInterface
  def removePlayer(player: PlayerInterface): PlayingFieldInterface
  def addBoard(board: BoardInterface): PlayingFieldInterface
  def removeBoard(board: BoardInterface): PlayingFieldInterface
  def setInnerField(table: TableInterface): PlayingFieldInterface
  def setStack(stack: TokenStackInterface): PlayingFieldInterface
  def setPlayers(players: List[PlayerInterface]): PlayingFieldInterface

  def withUpdated(boards: List[BoardInterface], innerField: TableInterface): PlayingFieldInterface
}
