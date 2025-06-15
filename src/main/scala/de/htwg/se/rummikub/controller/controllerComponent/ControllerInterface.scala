package de.htwg.se.rummikub.controller.controllerComponent

import de.htwg.se.rummikub.model.playingFieldComponent.{TokenStackInterface, PlayingFieldInterface}
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.gameModeComponent.GameModeTemplate

import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.{Row, Group}

import scala.swing.Publisher

trait ControllerInterface extends Publisher {
    def setupNewGame(amountPlayers: Int, names: List[String]): Unit
    def startGame: Unit
    def createTokenStack: TokenStackInterface
    def createRow(r: List[TokenInterface]): Row
    def createGroup(g: List[TokenInterface]): Group
    def setPlayingField(pf: Option[PlayingFieldInterface]): Unit
    def playingFieldToString: String
    def addTokenToPlayer(player: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, TokenStackInterface)
    def removeTokenFromPlayer(player: PlayerInterface, token: TokenInterface): Unit
    def addMultipleTokensToPlayer(player: PlayerInterface, stack: TokenStackInterface, amt: Int): (PlayerInterface, TokenStackInterface)
    def passTurn(state: GameStateInterface, ignoreFirstMoveCheck: Boolean): (GameStateInterface, String)
    def setNextPlayer(state: GameStateInterface): GameStateInterface
    def winGame: Boolean
    def addRowToTable(row: Row, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface)
    def addGroupToTable(group: Group, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface)
    def changeStringListToTokenList(list: List[String]): List[TokenInterface]
    def beginTurn(currentPlayer: PlayerInterface): Unit
    def getState: GameStateInterface
    def setStateInternal(state: GameStateInterface): Unit
    def executeAddRow(row: Row, player: PlayerInterface, stack: TokenStackInterface): Unit
    def executeAddGroup(group: Group, player: PlayerInterface, stack: TokenStackInterface): Unit
    def executeAppendToRow(token: TokenInterface, rowIndex: Int, player: PlayerInterface): Unit
    def executeAppendToGroup(token: TokenInterface, groupIndex: Int, player: PlayerInterface): Unit
    def undo: Unit
    def redo: Unit
    def drawFromStackAndPass: (GameStateInterface, String)
    def playRow(tokenStrings: List[String], currentPlayer: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, String)
    def playGroup(tokenStrings: List[String], currentPlayer: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, String)
    def appendTokenToRow(tokenString: String, index: Int): (PlayerInterface, String)
    def appendTokenToGroup(tokenString: String, index: Int): (PlayerInterface, String)
    def endGame: String
    def getGameEnded: Boolean
    def getGameMode: GameModeTemplate
    def getPlayingField: Option[PlayingFieldInterface]
    def setGameEnded(nge: Boolean): Unit
}



import scala.swing.event.Event

class UpdateEvent extends Event