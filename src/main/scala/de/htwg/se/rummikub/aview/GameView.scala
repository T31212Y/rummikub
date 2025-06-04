package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.controllerComponent.{ControllerInterface, UpdateEvent}

import scala.io.StdIn.readLine

trait GameView(controller: ControllerInterface) {
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

  def showAvailableCommands: Vector[String] = {
    Vector("Available commands:",
            "draw - Draw a token from the stack and pass your turn",
            "pass - Pass your turn",
            "row - Play a row of tokens",
            "group - Play a group of tokens",
            "appendToRow - Append a token to an existing row",
            "appendToGroup - Append a token to an existing group",
            "undo - Undo last move",
            "redo - Redo last undone move"
          )
  }
  
  def showGoodbye: String = {
    if (controller.getGameEnded) {
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

  def createNewGame: Unit

  def playGame: Unit

  def inputCommands(input: String): Unit = {
    input match {
        case "new"   => createNewGame
        case "start" => playGame
        case "help"  => showHelpPage.foreach(println)
        case "quit"  => println(showGoodbye)
        case _       => println("Invalid command.\n"); showHelpPage
    }
  }
}