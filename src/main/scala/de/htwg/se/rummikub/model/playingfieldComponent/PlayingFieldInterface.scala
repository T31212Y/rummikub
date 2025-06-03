package de.htwg.se.rummikub.model.playingfieldComponent

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

trait BoardInterface {
  def toStringRepresentation: String
}

trait TableInterface {
  def add(row: List[TokenStructureInterface]): TableInterface
  def remove(tokens: List[TokenStructureInterface]): TableInterface
  def getRow(index: Int): Option[List[TokenStructureInterface]]
  def updateRow(index: Int, newTokens: List[TokenStructureInterface]): TableInterface
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
}
