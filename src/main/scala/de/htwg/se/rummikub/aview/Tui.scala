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
            println("store - Store a token from the Table in storage")
            println("restore - Move a token from storage to a group on the table")
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
                if (currentPlayer.getCommandHistory.length > 0) {
                    val (newState, message) = controller.passTurn(controller.getState, false)
                    println(message)
                } else {
                    println("You must make a move before passing your turn.")
                }
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
            case "store" => {
                showTableTokensWithIndex()
                println("Enter the index of the token to store:")
                val input = readLine().trim

                try {
                    val index = input.toInt
                    controller.putTokenInStorage(index) match {
                        case Some(newState) =>
                            controller.setStateInternal(newState)
                            println(s"Token stored successfully.")
                        case None =>
                            println("Invalid token index!")
                    }
                } catch {
                    case _: NumberFormatException =>
                        println("Invalid input! Please enter a number.")
                }
            }

            case "restore" => {
                val storageTokens = controller.getState.getStorageTokens
                println("Tokens im Storage:")
                if (storageTokens.isEmpty) {
                    println("  (keine Tokens im Storage)")
                } else {
                    storageTokens.foreach(token => println(s"  $token"))
                }

                println("Enter the token string (e.g. '5:red') to move from Storage:")
                val tokenStr = readLine().trim

                println("Enter the group index:")
                val groupIndexInput = readLine().trim

                println("Enter the insert position in the group:")
                val insertAtInput = readLine().trim

                try {
                    val groupIndex = groupIndexInput.toInt
                    val insertAt = insertAtInput.toInt
                    val (newState, message) = controller.fromStorageToTable(controller.getState, tokenStr, groupIndex, insertAt)
                    controller.setStateInternal(newState)
                    println(message)
                } catch {
                    case _: NumberFormatException =>
                    println("Invalid input! Group index and insert position must be integers.")
                }
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
    def showTableTokensWithIndex(): Unit = {
        val indexed = controller.getState.getTable.getTokensOnTable.flatten.zipWithIndex
        println("Tokens on Table with Index:")
        indexed.foreach { case (token, idx) =>
            println(s"[$idx] $token")
        }
    }

}