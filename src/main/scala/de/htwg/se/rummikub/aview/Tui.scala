package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.model.{Player, PlayingField, TokenStack}

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
               "new - Start a new game",
               "start - Start a new game",
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
                println("Starting a new game...")
                println(askAmountOfPlayers())
                val amountPlayers: Int = readLine().toInt
                println(askPlayerNames())
                val players = readLine().split(",").map(_.trim).toList.map(name => Player(name, tokens = TokenStack().drawMultipleTokens(14)))
                new PlayingField(amountPlayers, players)
            }
            case "start" => {
                playingField.updatePlayingField()
            }
            // case "help" =>
            //case "solve" =>
            case "quit" => playingField
            case _ => playingField
        }
    }
    
    def showGoodbye(): String = {
        "Thank you for playing Rummikub! Goodbye!"
    }
}