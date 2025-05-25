package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Observer

import scala.io.StdIn.readLine

class Tui(controller: Controller) extends GameView with Observer {

    controller.add(this)

    override def showWelcome(): Vector[String] = {
        Vector("Welcome to",
        " ____                                _  _            _      _",
        "|  _ \\  _   _  _ __ ___   _ __ ___  (_)| | __ _   _ | |__  | |",
        "| |_) || | | || '_ ` _ \\ | '_ ` _ \\ | || |/ /| | | || '_ \\ | |",
        "|  _ < | |_| || | | | | || | | | | || ||   < | |_| || |_) ||_|",
        "|_| \\_\\\\___,_||_| |_| |_||_| |_| |_||_||_|\\_\\\\___,_||_.__/ (_)"
        )
    }

    override def showHelp(): String = {
        "Type 'help' for a list of commands."
    }

    override def showHelpPage(): Vector[String] = {
        Vector("Available commands:",
               "new - Create a new game",
               "start - Start the game",
               "quit - Exit the game"
        )
    }

    override def showGoodbye(): String = {
        "Thank you for playing Rummikub! Goodbye!"
    }

    override def start(): Unit = {
        var input = ""

        println(showWelcome().mkString("\n") + "\n")
        controller.setupNewGame(2, List("Emilia", "Noah"))

        while (input != "quit") {
            println(showHelp() + "\n")
            println("Please enter a command:")
            input = readLine()
            inputCommands(input)
        }
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
                    val amountPlayers = readLine().toInt

                    println(askPlayerNames())
                    val names = readLine().split(",").map(_.trim).toList

                    controller.setupNewGame(amountPlayers, names)
            }
            case "start" => playGame()
            case "help"  => println(showHelpPage().mkString("\n") + "\n")
            case "quit"  => println(showGoodbye())
            case _       =>
        }
    }

    private def playGame(): Unit = {
        println("Starting the game...")
        
        var stack = controller.createTokenStack()

        println("Drawing tokens for each player...")
        controller.playingField.map(_.players).getOrElse(List()).foreach { player =>
            controller.addMultipleTokensToPlayer(player, stack, 14)
        }

        var currentPlayer = controller.playingField match {
            case Some(field) if field.players.nonEmpty => field.players.head
            case _ =>
                println("No players available.")
                return
        }
        var gameInput = ""
        
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
            currentPlayer = currentPlayer.copy(commandHistory = currentPlayer.commandHistory :+ gameInput)
            currentPlayer = controller.processGameInput(gameInput, currentPlayer, stack)
        }
    }

    override def update: Unit = println(controller.playingfieldToString)
}