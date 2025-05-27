package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Observer

import scala.io.StdIn.readLine

class Tui(controller: Controller) extends Observer {

    controller.add(this)

    def showWelcome: Unit = {
        val logo = Vector("Welcome to",
        " ____                                _  _            _      _",
        "|  _ \\  _   _  _ __ ___   _ __ ___  (_)| | __ _   _ | |__  | |",
        "| |_) || | | || '_ ` _ \\ | '_ ` _ \\ | || |/ /| | | || '_ \\ | |",
        "|  _ < | |_| || | | | | || | | | | || ||   < | |_| || |_) ||_|",
        "|_| \\_\\\\___,_||_| |_| |_||_| |_| |_||_||_|\\_\\\\___,_||_.__/ (_)"
        )
        println(logo.mkString("\n") + "\n")
    }

    def showHelp: Unit = {
        println("Type 'help' for a list of commands.\n")
    }

    def showHelpPage: Unit = {
        val help = Vector("Available commands:",
               "new - Create a new game",
               "start - Start the game",
               "quit - Exit the game"
        )
        println(help.mkString("\n") + "\n")
    }

    def showGoodbye: Unit = {
        if (controller.gameEnded) {
            println("Thank you for playing Rummikub! Goodbye!")
        }
    }

    def askAmountOfPlayers: Unit = {
        println("Please enter the number of players (2-4):")
    }

    def askPlayerNames: Unit = {
        println("Please enter the names of the players (comma-separated):")
    }

    def createNewGame: Unit = {
        println("Creating a new game...")

        askAmountOfPlayers
        val amountPlayers = readLine().toInt

        askPlayerNames
        val names = readLine().split(",").map(_.trim).toList

        controller.setupNewGame(amountPlayers, names)
    }

    def inputCommands(input: String): Unit = {
        input match {
            case "new"   => createNewGame
            case "start" => playGame
            case "help"  => showHelpPage
            case "quit"  => showGoodbye
            case _       => println("Invalid command.\n"); showHelpPage
        }
    }

    def playGame: Unit = {
        println("Starting the game...")
        println("Drawing tokens for each player...")
        controller.startGame()
        
        var gameInput = ""
        
        while (!controller.winGame() && !controller.gameEnded) {
            val currentPlayer = controller.getState.currentPlayer
            val stack = controller.getState.currentStack

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
            case "draw" => {
                println("Drawing a token...")
                val (newState, message) = controller.drawFromStackAndPass
                println(message)
            }

            case "pass" => {
                val (newState, message) = controller.passTurn(controller.getState)
                controller.setStateInternal(newState)
                println(message)
            }

            case "group" => {
                println("Enter the tokens to play as group (e.g. 'token1:color, token2:color, ...'):")
                val tokenStrings = readLine().split(",").map(_.trim).toList
                val (newPlayer, message) = controller.playGroup(tokenStrings, controller.getState.currentPlayer, controller.getState.currentStack)
                println(message)

                val newState = controller.getState.updateCurrentPlayer(newPlayer)

                controller.setStateInternal(newState)
            }

            case "row" => {
                println("Enter the tokens to play as row (e.g. 'token1:color, token2:color, ...'):")
                val tokenStrings = readLine().split(",").map(_.trim).toList
                val (newPlayer, message) = controller.playRow(tokenStrings, controller.getState.currentPlayer, controller.getState.currentStack)
                println(message)

                val newState = controller.getState.updateCurrentPlayer(newPlayer)

                controller.setStateInternal(newState)
            }

            case "appendToRow" => {
                println("Enter the token to append (e.g. 'token1:color'):")
                val tokenInput = readLine().trim
                
                println("Enter the row's index (starting with 0):")
                val index = readLine().trim.toInt

                val (updatedPlayer, message) = controller.appendTokenToRow(tokenInput, index)
                println(message)

                val newState = controller.getState.updateCurrentPlayer(updatedPlayer)

                controller.setStateInternal(newState)
            }

            case "appendToGroup" => {
                println("Enter the token to append (e.g. 'token1:color'):")
                val tokenInput = readLine().trim

                println("Enter the group's index (starting with 0):")
                val index = readLine().trim.toInt

                val (updatedPlayer, message) = controller.appendTokenToGroup(tokenInput, index)
                println(message)

                val newState = controller.getState.updateCurrentPlayer(updatedPlayer)

                controller.setStateInternal(newState)
            }

            case "undo" => {
                controller.undo()
                println("Undo successful.")
            }

            case "redo" => {
                controller.redo()
                println("Redo successful.")
            }

            case "end" => {
                println("Exiting the game...")
                controller.gameEnded = true
            }

            case _ => println("Invalid command.")
        }
    }

    override def update: Unit = println(controller.playingfieldToString)
}