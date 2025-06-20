package de.htwg.se.rummikub.controller.controllerComponent

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.{BoardInterface, TokenStackInterface, TableInterface}

trait GameStateInterface {
    def getTable: TableInterface
    def getPlayers: Vector[PlayerInterface]
    def getBoards: Vector[BoardInterface]
    def getCurrentPlayerIndex: Int
    def getFinalRoundsLeft: Option[Int]

    def currentPlayer: PlayerInterface
    def currentBoard: BoardInterface
    def currentStack: TokenStackInterface
    def updateCurrentPlayer(updatedPlayer: PlayerInterface): GameStateInterface
    def updateCurrentBoard(updatedBoard: BoardInterface): GameStateInterface
    def updatePlayerById(id: String, updated: PlayerInterface): GameStateInterface
    def updateBoardByIndex(index: Int, updated: BoardInterface): GameStateInterface
    def updateTable(newTable: TableInterface): GameStateInterface
    def updateStack(newStack: TokenStackInterface): GameStateInterface
    def updatePlayerIndex(newIndex: Int): GameStateInterface
    def updatePlayers(newPlayers: Vector[PlayerInterface]): GameStateInterface
    def nextTurn: GameStateInterface

    def updated(newPlayers: Vector[PlayerInterface], newStack: TokenStackInterface, newFinalRoundsLeft: Option[Int]): GameStateInterface

    def getStorageTokens: Vector[String]
    def updatedStorage(newStorage: Vector[String]): GameStateInterface
}