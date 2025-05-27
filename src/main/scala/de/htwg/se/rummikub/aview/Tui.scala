package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Observer

import scala.io.StdIn.readLine

class Tui(controller: Controller) extends Observer {

    controller.add(this)

    def inputCommands(input: String): Unit = {
        input match {
            case "new" => controller.createNewGame
            case "start" => playGame
            case "help"  => println(controller.showHelpPage().mkString("\n") + "\n")
            case "quit"  => println(controller.showGoodbye())
            case _       =>
        }
    }

    def playGame: Unit = {
        println("Starting the game...")
        println("Drawing tokens for each player...")
        controller.startGame()
        
        var gameInput = ""
        var currentPlayer = controller.getState.currentPlayer
        val stack = controller.getState.currentStack
        
        while (!controller.winGame() && gameInput != "end") {
            controller.setPlayingField(controller.gameMode.updatePlayingField(controller.playingField))
            println(currentPlayer.name + ", it's your turn!\n")
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

        val updatedPlayer = currentPlayer.copy(commandHistory = currentPlayer.commandHistory :+ gameInput)
        controller.setStateInternal(controller.getState.updateCurrentPlayer(updatedPlayer))

        input match {
            case "group" => {
                println("Enter the tokens to play as group (e.g. 'token1:color, token2:color, ...'):")
                val tokenStrings = readLine().split(",").map(_.trim).toList
                val (newPlayer, message) = controller.playGroup(tokenStrings, controller.getState.currentPlayer, controller.getState.currentStack)
                println(message)

                val newState = controller.getState.updateCurrentPlayer(newPlayer)

                controller.setStateInternal(newState)
            }
            case "row"   => {
                println("Enter the tokens to play as row (e.g. 'token1:color, token2:color, ...'):")
                val tokenStrings = readLine().split(",").map(_.trim).toList
                val (newPlayer, message) = controller.playRow(tokenStrings, controller.getState.currentPlayer, controller.getState.currentStack)
                println(message)

                val newState = controller.getState.updateCurrentPlayer(newPlayer)

                controller.setStateInternal(newState)
            }
            /*case "appendToRow" => controller.appendToRow(player, stack)
            case "appendToGroup" => controller.appendToGroup(player, stack)
            case "draw" => controller.drawToken(player, stack)
            case "undo" => controller.undoMove(player)
            case "redo" => controller.redoMove(player)
            case "pass" => controller.passTurn(player)*/
            case _ =>
        }
    }

    override def update: Unit = println(controller.playingfieldToString)
}