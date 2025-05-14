package de.htwg.se.rummikub.controller

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.util.Observable

import scala.io.StdIn.readLine
import scala.compiletime.uninitialized


class Controller(var gameMode: GameModeTemplate) extends Observable {

    var playingField: PlayingField = uninitialized

    def setupNewGame(amountPlayers: Int, names: List[String]): Unit = {
        gameMode = GameModeFactory.createGameMode(amountPlayers, names)
        val players = gameMode.createPlayers()
        playingField = gameMode.createPlayingField(players)
        notifyObservers
    }

    def createTokenStack(): TokenStack = {
        TokenStack()
    }

    def createRow(r: List[String]): Row = {
        Row(r)
    }

    def createGroup(g: List[String]): Group = {
        Group(g)
    }

    def updatePlayingField(): Unit = {
        playingField = gameMode.updatePlayingField(playingField)
        notifyObservers
    }

    def playingfieldToString: String = {
        gameMode.renderPlayingField(playingField)
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

    def addRowToTable(row: Row, currentPlayer: Player): List[Token] = {
        if (row.rowTokens.forall(token => currentPlayer.tokens.contains(token))) {
            playingField = playingField.copy(innerField = playingField.innerField.add(row.rowTokens))
            row.rowTokens
        } else {
            println("You can only play tokens that are on your board.")
            List.empty[Token]
        }
    }

    def addGroupToTable(group: Group, currentPlayer: Player): List[Token] = {
        if (group.groupTokens.forall(token => currentPlayer.tokens.contains(token))) {
            playingField = playingField.copy(innerField = playingField.innerField.add(group.groupTokens))
            group.groupTokens
        } else {
            println("You can only play tokens that are on your board.")
            List.empty[Token]
        }
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
            val removeTokens = addRowToTable(createRow(tokens), currentPlayer)
            removeTokens.foreach(t => removeTokenFromPlayer(currentPlayer, t))
            currentPlayer

        case "group" => 
            println("Enter the tokens to play as group (e.g. 'token1:color, token2:color, ...'):")
            val tokens = readLine().split(",").map(_.trim).toList
            val removeTokens = addGroupToTable(createGroup(tokens), currentPlayer)
            removeTokens.foreach(t => removeTokenFromPlayer(currentPlayer, t))
            currentPlayer

        case "addToRow" => 
            println("Enter the tokens to add to the row (e.g. 'token1:color, token2:color, ...'):")
            val tokens = readLine().split(",").map(_.trim).toList
            val removeTokens = addRowToTable(createRow(tokens), currentPlayer)
            removeTokens.foreach(t => removeTokenFromPlayer(currentPlayer, t))
            currentPlayer

        case "addToGroup" => 
            println("Enter the tokens to add to the group (e.g. 'token1:color, token2:color, ...'):")
            val tokens = readLine().split(",").map(_.trim).toList
            val removeTokens = addGroupToTable(createGroup(tokens), currentPlayer)
            removeTokens.foreach(t => removeTokenFromPlayer(currentPlayer, t))
            currentPlayer

        case "end" => 
            println("Exiting the game...")
            currentPlayer

        case _ => 
            println("Invalid command.")
            currentPlayer
        }
    }
}