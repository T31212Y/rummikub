package de.htwg.se.rummikub.controller.controllerComponent

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.model.playingfieldComponent.PlayingFieldInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface
import de.htwg.se.rummikub.model.playingfieldComponent.{TokenStackInterface, TableInterface}

trait ControllerInterface {
  def setupNewGame(amountPlayers: Int, names: List[String]): Unit
  def startGame(): Unit
  def setPlayingField(pf: Option[PlayingFieldInterface]): Unit
  def playingfieldToString: String
  def addTokenToPlayer(player: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, TokenStackInterface)
  def removeTokenFromPlayer(player: PlayerInterface, token: TokenInterface): Unit
  def addMultipleTokensToPlayer(player: PlayerInterface, stack: TokenStackInterface, amt: Int): (PlayerInterface, TokenStackInterface)
  def passTurn(state: GameState, ignoreFirstMoveCheck: Boolean = false): (GameState, String)
  def winGame(): Boolean
  def addRowToTable(row: TokenStructureInterface, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface)
  def addGroupToTable(group: TokenStructureInterface, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface)
  def changeStringListToTokenList(list: List[String]): List[TokenInterface]
  def beginTurn(currentPlayer: PlayerInterface): Unit
  def getState: GameState
  def setStateInternal(state: GameState): Unit
  def executeAddRow(row: TokenStructureInterface, player: PlayerInterface, stack: TokenStackInterface): Unit
  def executeAddGroup(group: TokenStructureInterface, player: PlayerInterface, stack: TokenStackInterface): Unit
  def executeAppendToRow(token: TokenInterface, rowIndex: Int, player: PlayerInterface): Unit
  def executeAppendToGroup(token: TokenInterface, groupIndex: Int, player: PlayerInterface): Unit
  def undo(): Unit
  def redo(): Unit
  def drawFromStackAndPass: (GameState, String)
  def playRow(tokenStrings: List[String], currentPlayer: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, String)
  def playGroup(tokenStrings: List[String], currentPlayer: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, String)
  def appendTokenToRow(tokenString: String, index: Int): (PlayerInterface, String)
  def appendTokenToGroup(tokenString: String, index: Int): (PlayerInterface, String)
  def endGame(): Unit
}
