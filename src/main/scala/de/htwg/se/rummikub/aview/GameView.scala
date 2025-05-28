package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import scala.io.StdIn.readLine

trait GameView(controller: Controller) {
  def showWelcome: Vector[String] = {
    Vector("Welcome to",
    " ____                                _  _            _      _",
    "|  _ \\  _   _  _ __ ___   _ __ ___  (_)| | __ _   _ | |__  | |",
    "| |_) || | | || '_ ` _ \\ | '_ ` _ \\ | || |/ /| | | || '_ \\ | |",
    "|  _ < | |_| || | | | | || | | | | || ||   < | |_| || |_) ||_|",
    "|_| \\_\\\\___,_||_| |_| |_||_| |_| |_||_||_|\\_\\\\___,_||_.__/ (_)"
    )
  }

  def showHelp: String = {
        "Type 'help' for a list of commands.\n"
  }

  def showHelpPage: Vector[String] = {
      Vector("Available commands:",
              "new - Create a new game",
              "start - Start the game",
              "quit - Exit the game"
      )
  }
  
  def showGoodbye: String = {
    if (controller.gameEnded) {
      "Thank you for playing Rummikub! Goodbye!"
    } else {
      ""
    }
  }

  def askAmountOfPlayers: String = {
    "Please enter the number of players (2-4):"
  }

  def askPlayerNames: String = {
    "Please enter the names of the players (comma-separated):"
  }

  def createNewGame: Unit = {
      println("Creating a new game...")

      askAmountOfPlayers
      val amountPlayers = readLine().toInt

      askPlayerNames
      val names = readLine().split(",").map(_.trim).toList

      controller.setupNewGame(amountPlayers, names)
  }

  def playGame: Unit

  def inputCommands(input: String): Unit = {
    input match {
        case "new"   => createNewGame
        case "start" => playGame
        case "help"  => showHelpPage
        case "quit"  => showGoodbye
        case _       => println("Invalid command.\n"); showHelpPage
    }
  }
}