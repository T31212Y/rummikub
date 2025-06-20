package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.controllerComponent.{ControllerInterface, UpdateEvent}

import scala.io.StdIn.readLine
import scala.swing.Reactor

class Tui(controller: ControllerInterface) extends Reactor with GameView(controller) {

    listenTo(controller)

    override def createNewGame: Unit = {
        println("Creating a new game...")

        askAmountOfPlayers
        println(askAmountOfPlayers)
        val amountPlayers = readLine().toInt

        askPlayerNames
        println(askPlayerNames)
        val names = readLine().split(",").map(_.trim).toList

        controller.setupNewGame(amountPlayers, names)
    }
    override def playGame: Unit = {
        println("Starting the game...")
        println("Drawing tokens for each player...")
        controller.startGame
        
        var gameInput = ""
        
        while (!controller.winGame && !controller.getGameEnded) {
            val currentPlayer = controller.getState.currentPlayer
            val stack = controller.getState.currentStack

            controller.setPlayingField(controller.getGameMode.updatePlayingField(controller.getPlayingField))
            println(currentPlayer.getName + ", it's your turn!\n")
            println(s"Remaining tokens in stack: ${controller.getState.currentStack.size}\n")
            println("Available commands:")
            println("group - Play a group of tokens")
            println("row - Play a row of tokens")
            println("appendToRow - Append a token to an existing row")
            println("appendToGroup - Append a token to an existing group")
            println("draw - Draw a token from the stack and pass your turn")
            println("undo - Undo last move")
            println("redo - Redo last undone move")
            println("pass - Pass your turn")
            println("end - End the game\n")

            controller.beginTurn(currentPlayer)

            gameInput = readLine()
            processGameInput(gameInput)
        }
    }

    def processGameInput(input: String): Unit = {
        val currentPlayer = controller.getState.currentPlayer
        val stack = controller.getState.currentStack
        val gameInput = input.toLowerCase.trim

        val updatedPlayer = currentPlayer.updated(newTokens = currentPlayer.getTokens, newCommandHistory = currentPlayer.getCommandHistory :+ gameInput, newHasCompletedFirstMove = currentPlayer.getHasCompletedFirstMove)
        controller.setStateInternal(controller.getState.updateCurrentPlayer(updatedPlayer))

        input match {
            case "draw" => {
                println("Drawing a token...")
                val (newState, message) = controller.drawFromStackAndPass
                println(message)
            }

            case "pass" => {
                val (newState, message) = controller.passTurn(controller.getState, false)
                println(message)
            }

            case "group" => {
                println("Enter the tokens to play as group (e.g. 'token1:color, token2:color, ...'):")
                val tokenStrings = readLine().split(",").map(_.trim).toList
                val (newPlayer, message) = controller.playGroup(tokenStrings, controller.getState.currentPlayer, controller.getState.currentStack)
                println(message)
            }

            case "row" => {
                println("Enter the tokens to play as row (e.g. 'token1:color, token2:color, ...'):")
                val tokenStrings = readLine().split(",").map(_.trim).toList
                val (newPlayer, message) = controller.playRow(tokenStrings, controller.getState.currentPlayer, controller.getState.currentStack)
                println(message)
            }

            case "appendToRow" => {
                println("Enter the token to append (e.g. 'token1:color'):")
                val tokenInput = readLine().trim
                
                println("Enter the row's index (starting with 0):")
                val index = readLine().trim.toInt

                val (updatedPlayer, message) = controller.appendTokenToRow(tokenInput, index)
                println(message)
            }

            case "appendToGroup" => {
                println("Enter the token to append (e.g. 'token1:color'):")
                val tokenInput = readLine().trim

                println("Enter the group's index (starting with 0):")
                val index = readLine().trim.toInt

                val (updatedPlayer, message) = controller.appendTokenToGroup(tokenInput, index)
                println(message)
            }

            case "undo" => {
                controller.undo
                println("Undo successful.")
            }

            case "redo" => {
                controller.redo
                println("Redo successful.")
            }

            case "end" => {
                println("Exiting the game...")
                controller.setGameEnded(true)
            }

            case _ => println("Invalid command.")
        }
    }

    reactions += {
        case _: UpdateEvent =>
            println(controller.playingFieldToString)
    }
}