package de.htwg.se.rummikub.controller.controllerComponent

import de.htwg.se.rummikub.util.UndoManager

import de.htwg.se.rummikub.model.playingFieldComponent.{TokenStackInterface, PlayingFieldInterface}
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.gameModeComponent.GameModeTemplate
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

import scala.swing.Publisher

trait ControllerInterface extends Publisher {
    def setupNewGame(amountPlayers: Int, names: List[String]): Unit
    def startGame: Unit
    def setPlayingField(pf: Option[PlayingFieldInterface]): Unit
    def playingFieldToString: String
    def addTokenToPlayer(player: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, TokenStackInterface)
    def removeTokenFromPlayer(player: PlayerInterface, token: TokenInterface): Unit
    def addMultipleTokensToPlayer(player: PlayerInterface, stack: TokenStackInterface, amt: Int): (PlayerInterface, TokenStackInterface)
    def passTurn(state: GameStateInterface, ignoreFirstMoveCheck: Boolean): (GameStateInterface, String)
    def setNextPlayer(state: GameStateInterface): GameStateInterface
    def winGame: Boolean
    def addRowToTable(row: TokenStructureInterface, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface)
    def addGroupToTable(group: TokenStructureInterface, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface)
    def changeStringListToTokenList(list: List[String]): List[TokenInterface]
    def beginTurn(currentPlayer: PlayerInterface): Unit
    def getState: GameStateInterface
    def setStateInternal(state: GameStateInterface): Unit
    def executeAddRow(row: TokenStructureInterface, player: PlayerInterface, stack: TokenStackInterface): Unit
    def executeAddGroup(group: TokenStructureInterface, player: PlayerInterface, stack: TokenStackInterface): Unit
    def executeAppendToRow(token: TokenInterface, rowIndex: Int, insertAt: Int, player: PlayerInterface): Unit
    def executeAppendToGroup(token: TokenInterface, groupIndex: Int, insertAt: Int, player: PlayerInterface): Unit
    def undo: Unit
    def redo: Unit
    def drawFromStackAndPass: (GameStateInterface, String)
    def playRow(tokenStrings: List[String], currentPlayer: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, String)
    def playGroup(tokenStrings: List[String], currentPlayer: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, String)
    def appendTokenToRow(tokenString: String, rowIndex: Int, insertAt: Int): (PlayerInterface, String)
    def appendTokenToGroup(tokenString: String, groupIndex: Int, insertAt: Int): (PlayerInterface, String)
    def endGame: String
    def getGameEnded: Boolean
    def getGameMode: GameModeTemplate
    def getPlayingField: Option[PlayingFieldInterface]
    def setGameEnded(nge: Boolean): Unit
    def getTurnStartState: Option[GameStateInterface]
    def setTurnStartState(newState: Option[GameStateInterface]): Unit
    def getUndoManager: UndoManager
    def setUndoManager(num: UndoManager): Unit
    def getGameStarted: Boolean
    def setGameStarted(ngs: Boolean): Unit
    def getTokenFromString(tokenStr: String): TokenInterface
    def putTokenInStorage(tokenId: Int): Option[GameStateInterface]
    def getFormattedTokensOnTableWithLabels: String
    def fromStorageToTable(state: GameStateInterface, tokenStr: String, groupIndex: Int, insertAtIndex: Int): (GameStateInterface, String)
    def getDisplayStringForTokensWithIndex: String
}



import scala.swing.event.Event

class UpdateEvent extends Event