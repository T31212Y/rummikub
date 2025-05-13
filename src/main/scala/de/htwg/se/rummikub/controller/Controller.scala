package de.htwg.se.rummikub.controller

import de.htwg.se.rummikub.model.{Player, PlayingField, TokenStack, Row, Group, Token, Joker}
import de.htwg.se.rummikub.util.Observable
import scala.io.StdIn.readLine


class Controller(var playingField: PlayingField) extends Observable {

    def createPlayingField(amountOfPlayers: Int, players: List[Player]): Unit = {
        playingField = PlayingField(amountOfPlayers, players)
        notifyObservers
    }

    def createTokenStack(): TokenStack = {
        TokenStack()
    }

    def createPlayer(name: String): Player = {
        Player(name)
    }

    def createRow(r: List[String]): Row = {
        Row(r)
    }

    def createGroup(g: List[String]): Group = {
        Group(g)
    }

    def setPlayingField(playingField: PlayingField): Unit = {
        this.playingField = playingField
        notifyObservers
    }

    def playingfieldToString: String = {
        playingField.toString().replace("x", " ").replace("y", " ")
    }

    def addTokenToPlayer(player: Player, stack: TokenStack): Unit = {
        val tokenToAdd = stack.drawToken()
        playingField = playingField.copy(players = playingField.players.map {
            case p if p.name == player.name => p.copy(tokens = p.tokens :+ tokenToAdd)
            case p => p
        })
    }

    def removeTokenFromPlayer(player: Player, token: Token): Unit = {
        playingField = playingField.copy(players = playingField.players.map {
            case p if p.name == player.name => p.copy(tokens = p.tokens.filterNot(_.equals(token)))
            case p => p
        })
    }

    def addMultipleTokensToPlayer(player: Player, stack: TokenStack, amt: Int): Unit = {
        val tokensToAdd = stack.drawMultipleTokens(amt)
        playingField = playingField.copy(players = playingField.players.map {
            case p if p.name == player.name => p.copy(tokens = p.tokens ++ tokensToAdd)
            case p => p
        })
    }
    
    def passTurn(currentPlayer: Player): Player = {
        val nextPlayer = setNextPlayer(currentPlayer).copy(commandHistory = List(""))
        playingField = playingField.copy(players = playingField.players.map {
            case p if p.name == nextPlayer.name => nextPlayer
            case p => p
        })
        println(currentPlayer.name + " passed the turn to " + nextPlayer.name)
        nextPlayer
    }

    def setNextPlayer(p: Player): Player = {
        val current = playingField.players.indexWhere(_.name == p.name)
        if (current == playingField.players.size - 1) playingField.players.head else playingField.players(current + 1)
    }

    def winGame(): Boolean = {
        playingField.players.exists(player => player.tokens.isEmpty) match {
            case true => {
                println("Player " + playingField.players.find(_.tokens.isEmpty).get.name + " wins the game!")
                true
            }
            case false => false
        }
    }

    def addRowToTable(row: Row): List [Token] = {
        val updatedPlayingField = {
            if (playingField.amountOfPlayers == 2) {
                playingField.copy(innerField2Players = playingField.innerField2Players.add(row.rowTokens))
            } else {
                playingField.copy(innerField34Players = playingField.innerField34Players.add(row.rowTokens))
            }
        }
        playingField = updatedPlayingField
        row.rowTokens
    }

    def addGroupToTable(group: Group): List[Token] = {
        val updatedPlayingField = {
            if (playingField.amountOfPlayers == 2) {
                playingField.copy(innerField2Players = playingField.innerField2Players.add(group.groupTokens))
            } else {
                playingField.copy(innerField34Players = playingField.innerField34Players.add(group.groupTokens))
            }
        }
        playingField = updatedPlayingField
        group.groupTokens
    }

    def processGameInput(gameInput: String, currentPlayer: Player, stack: TokenStack): Player = {
        gameInput match {
        case "draw" => 
            println("Drawing a token...")
            addTokenToPlayer(currentPlayer, stack)
            passTurn(currentPlayer)
        
        case "pass" => 
            if (currentPlayer.commandHistory.size <= 1) {
                println("You cannot pass your turn without playing a token.")
                currentPlayer
            } else {
                passTurn(currentPlayer)
            }
            
        case "row" => 
            println("Enter the tokens to play as row (e.g. 'token1:color, token2:color, ...'):")
            val tokens = readLine().split(",").map(_.trim).toList
            val removeTokens = addRowToTable(createRow(tokens))
            println(removeTokens)
            removeTokens.foreach(t => removeTokenFromPlayer(currentPlayer, t))
            println(currentPlayer.tokens)
            currentPlayer

        case "group" => 
            println("Enter the tokens to play as group (e.g. 'token1:color, token2:color, ...'):")
            val tokens = readLine().split(",").map(_.trim).toList
            val removeTokens = addGroupToTable(createGroup(tokens))
            println(removeTokens)
            removeTokens.foreach(t => removeTokenFromPlayer(currentPlayer, t))
            println(currentPlayer.tokens)
            currentPlayer

        case "end" => 
            println("Exiting the game...")
            currentPlayer

        case _ => 
            println("Invalid command.")
            currentPlayer
        }
    }
    def setupNewGame(amountPlayers: Int, names: List[String]): Unit = {
        val players = names.map(createPlayer)
        createPlayingField(amountPlayers, players)
    }
}