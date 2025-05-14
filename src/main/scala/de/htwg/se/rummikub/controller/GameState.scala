package de.htwg.se.rummikub.controller

import de.htwg.se.rummikub.model.{Row, Group, Token}

object GameState extends Enumeration {
    type GameState = Value

    val Start, PlayerTurn, ValidateMove, EndTurn, GameOver = Value

    private var currentState: GameState = Start

    def getState: GameState = currentState

    def setState(newState: GameState): Unit = {
        currentState = newState
    }

    def isGameOver: Boolean = currentState == GameOver


    def addToRow(row: Row, token: Token): Unit = {
        if (currentState == PlayerTurn) {
            println(s"Throwing token to row: $token")
            row.addToken(token) 
            setState(ValidateMove) 
        } else {
            println("Cannot throw a token in the current state.")
        }
    }

    def removeFromRow(row: Row, token: Token): Unit = {
        if (currentState == PlayerTurn) {
            println(s"Removing token from row: $token")
            row.removeToken(token)
        } else {
            println("Cannot remove a token in the current state.")
        }
    }

    def addToGroup(group: Group, token: Token): Unit = {
        if (currentState == PlayerTurn) {
            println(s"Throwing token to group: $token")
            group.addToken(token) 
            setState(ValidateMove) 
        } else {
            println("Cannot throw a token in the current state.")
        }
    }

    def removeFromGroup(group: Group, token: Token): Unit = {
        if (currentState == PlayerTurn) {
            println(s"Removing token from group: $token")
            group.removeToken(token) 
        } else {
            println("Cannot remove a token in the current state.")
        }
    }

    def startGame(): Unit = {
        if (currentState == Start) {
            println("Game is starting.")
            setState(PlayerTurn) 
        } else {
            println("Game has already started.")
        }
    }

    def endTurn(): Unit = {
        if (currentState == ValidateMove) {
            println("Ending the turn.")
            setState(EndTurn) 
        } else {
            println("Cannot end the turn in the current state.")
        }
    }
}

