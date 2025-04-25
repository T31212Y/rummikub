package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.model.{Player, PlayingField, TokenStack, Row, Group}
import de.htwg.se.rummikub.util.Observable


import scala.io.StdIn.readLine

class Tui {
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

    def inputCommands(input: String, playingField: PlayingField): PlayingField = {
        input match {
            case "new" => {
                println("Creating a new game...")
                println(askAmountOfPlayers())
                val amountPlayers: Int = readLine().toInt
                println(askPlayerNames())
                val players = readLine().split(",").map(_.trim).toList.map(name => Player(name))
                new PlayingField(amountPlayers, players)
            }
            case "start" => {
                playGame(playingField)
            }
            case "help" => {
                println(showHelpPage().mkString("\n") + "\n")
                playingField
            }
            case "quit" => playingField
            case _ => playingField
        }
    }

    def playGame(playingField: PlayingField): PlayingField = {
        println("Starting the game...")

        val stack = new TokenStack()
        var pf = playingField
        var currentPlayer = pf.player1
        var gameInput = ""

        println("Drawing tokens for each player...")
        pf.players.foreach { player =>
            val drawnTokens = stack.drawMultipleTokens(14)
            val p = player.copy(tokens = player.tokens ++ drawnTokens)
            p
        }

        while (!winGame(pf.players) && gameInput != "end") {
            pf = pf.updatePlayingField()
            println(pf.toString())
            println(currentPlayer.name + ", it's your turn!\n")
            println("Available commands:")
            println("group - Play a group of tokens")
            println("row - Play a row of tokens")
            println("draw - Draw a token from the stack and pass your turn")
            println("pass - Pass your turn")
            println("end - End the game\n")

            gameInput = readLine()
            currentPlayer = currentPlayer.copy(commandHistory = currentPlayer.commandHistory:+ gameInput)

            gameInput match {
                case "draw" => {
                    println("Drawing a token...")
                    val drawnToken = stack.drawToken()
                    currentPlayer = currentPlayer.copy(tokens = currentPlayer.tokens :+ drawnToken)
                    currentPlayer = passTurn(pf, currentPlayer)
                }
                case "pass" => {
                    if (currentPlayer.commandHistory.size == 1) {
                        println("You cannot pass your turn without playing a token.")
                    } else {
                        currentPlayer = passTurn(pf, currentPlayer)
                    }
                }
                case "row" => {
                    println("Enter the tokens to play as row (e.g. 'token1:color, token2:color, ...'):")
                    val rowInput = new Row(readLine().split(",").map(_.trim).toList)
                    val removeTokens = pf.addTableRow(rowInput)
                    currentPlayer = currentPlayer.copy(tokens = currentPlayer.tokens.filterNot(token => removeTokens.contains(token)))
                }
                case "group" => {
                    println("Enter the tokens to play as group (e.g. 'token1:color, token2:color, ...'):")
                    val groupInput = new Group(readLine().split(",").map(_.trim).toList)
                    val removeTokens = pf.addTableGroup(groupInput)
                    currentPlayer = currentPlayer.copy(tokens = currentPlayer.tokens.filterNot(token => removeTokens.contains(token)))
                }
                case "end" => println("Exiting the game...")
                case _ => println("Invalid command.")
            }
        }
        pf
    }

    def passTurn(playingField: PlayingField, currentPlayer: Player): Player = {
        val nextPlayer = playingField.nextPlayer(currentPlayer)
        println(currentPlayer.name + " passed the turn to " + nextPlayer.name)
        //currentPlayer = currentPlayer.copy(commandHistory = List(""))
        nextPlayer
    }

    def winGame(players: List[Player]): Boolean = {
        players.exists(player => player.tokens.isEmpty) match {
            case true => {
                println("Player " + players.find(_.tokens.isEmpty).get.name + " wins the game!")
                true
            }
            case false => false
        }
    }
    
    def showGoodbye(): String = {
        "Thank you for playing Rummikub! Goodbye!"
    }
}