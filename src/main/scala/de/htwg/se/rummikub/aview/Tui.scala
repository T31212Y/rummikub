package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Observer

import scala.io.StdIn.readLine

class Tui(controller: Controller) extends Observer {

    controller.add(this)

    def showWelcome(): Vector[String] = {
        Vector("Welcome to",
        " ____                                _  _            _      _",
        "|  _ \\  _   _  _ __ ___   _ __ ___  (_)| | __ _   _ | |__  | |",
        "| |_) || | | || '_ ` _ \\ | '_ ` _ \\ | || |/ /| | | || '_ \\ | |",
        "|  _ < | |_| || | | | | || | | | | || ||   < | |_| || |_) ||_|",
        "|_| \\_\\\\___,_||_| |_| |_||_| |_| |_||_||_|\\_\\\\___,_||_.__/ (_)"
        )
    }

    def showHelp(): String = {
        "Type 'help' for a list of commands."
    }
    
    def showHelpPage(): Vector[String] = {
        Vector("Available commands:",
               "new - Create a new game",
               "start - Start the game",
               "quit - Exit the game"
              )
    }

    def askAmountOfPlayers(): String = {
        "Please enter the number of players (2-4):"
    }

    def askPlayerNames(): String = {
        "Please enter the names of the players (comma-separated):"
    }

    def inputCommands(input: String): Unit = {
        input match {
            case "new" => {
                println("Creating a new game...")

                println(askAmountOfPlayers())
                val amountPlayers: Int = readLine().toInt

                println(askPlayerNames())
                val players = readLine().split(",").map(_.trim).toList.map(name => controller.createPlayer(name))

                controller.createPlayingField(amountPlayers, players)
            }
            case "start" => {
                playGame()
            }
            case "help" => {
                println(showHelpPage().mkString("\n") + "\n")
            }
            case "quit" => println(showGoodbye())
            case _ =>
        }
    }

    def playGame(): Unit = {
        println("Starting the game...")

        var stack = controller.createTokenStack()
        var currentPlayer = controller.playingField.player1
        var gameInput = ""

        println("Drawing tokens for each player...")
        controller.playingField.players.foreach(p => controller.addMultipleTokensToPlayer(p, stack, 14))

        while (!controller.winGame() && gameInput != "end") {
            controller.setPlayingField(controller.playingField.updatePlayingField())
            println(currentPlayer.name + ", it's your turn!\n")
            println("Available commands:")
            println("group - Play a group of tokens")
            println("row - Play a row of tokens")
            println("draw - Draw a token from the stack and pass your turn")
            println("pass - Pass your turn")
            println("end - End the game\n")

            gameInput = readLine()
            currentPlayer = currentPlayer.copy(commandHistory = currentPlayer.commandHistory :+ gameInput)
            currentPlayer = controller.processGameInput(gameInput, currentPlayer, stack)
        }
            /*gameInput match {
                case "draw" => {
                    println("Drawing a token...")
                    controller.addTokenToPlayer(currentPlayer, stack)
                    currentPlayer = controller.passTurn(currentPlayer)
                }
                case "pass" => {
                    if (currentPlayer.commandHistory.size == 1) {
                        println("You cannot pass your turn without playing a token.")
                    } else {
                        currentPlayer = controller.passTurn(currentPlayer)
                    }
                }
                case "row" => {
                    println("Enter the tokens to play as row (e.g. 'token1:color, token2:color, ...'):")
                    val removeTokens = controller.addRowToTable(controller.createRow(readLine().split(",").map(_.trim).toList))
                    removeTokens.foreach(t => controller.removeTokenFromPlayer(currentPlayer, t))
                }
                case "group" => {
                    println("Enter the tokens to play as group (e.g. 'token1:color, token2:color, ...'):")
                    val removeTokens = controller.addGroupToTable(controller.createGroup(readLine().split(",").map(_.trim).toList))
                    removeTokens.foreach(t => controller.removeTokenFromPlayer(currentPlayer, t))
                }
                case "end" => println("Exiting the game...")
                case _ => println("Invalid command.")
            }
        }*/
        def inputCommands(input: String): Unit = {
            input match {
                case "new" => {
                    println("Creating a new game...")

                    println(askAmountOfPlayers())
                    val amountPlayers = readLine().toInt

                    println(askPlayerNames())
                    val names = readLine().split(",").map(_.trim).toList

                    controller.setupNewGame(amountPlayers, names)
                }
                case "start" => playGame()
                case "help"  => println(showHelpPage().mkString("\n") + "\n")
                case "quit"  => println(showGoodbye())
                case _       => println("Unknown command. Type 'help' for available commands.")
            }
        }
    }

    def showGoodbye(): String = {
        "Thank you for playing Rummikub! Goodbye!"
    }

    override def update: Unit = println(controller.playingfieldToString)
}