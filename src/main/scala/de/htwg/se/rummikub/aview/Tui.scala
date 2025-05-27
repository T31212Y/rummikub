package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Observer

import scala.io.StdIn.readLine

class Tui(controller: Controller) extends Observer {

    controller.add(this)

    def inputCommands(input: String): Unit = {
        input match {
            case "new" => controller.createNewGame
            case "start" => playGame()
            case "help"  => println(controller.showHelpPage().mkString("\n") + "\n")
            case "quit"  => println(controller.showGoodbye())
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