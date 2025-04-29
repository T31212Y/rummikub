package de.htwg.se.rummikub.controller

import de.htwg.se.rummikub.model.{Player, PlayingField, TokenStack, Row, Group, Token, Joker}
import de.htwg.se.rummikub.util.Observable

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

    def removeTokenFromPlayer(player: Player, token: Token | Joker): Unit = {
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

    def addRowToTable(row: Row): List [Token | Joker] = {
        val updatedPlayingField = {
            if (playingField.amountOfPlayers == 2) {
                playingField.copy(innerField2Players = playingField.innerField2Players.add(row.rowTokens))
            } else if (playingField.amountOfPlayers > 2) {
                playingField.copy(innerField34Players = playingField.innerField34Players.add(row.rowTokens))
            } else {
                playingField
            }
        }
        playingField = updatedPlayingField
        row.rowTokens
    }

    def addGroupToTable(group: Group): List[Token | Joker] = {
        val updatedPlayingField = {
            if (playingField.amountOfPlayers == 2) {
                playingField.copy(innerField2Players = playingField.innerField2Players.add(group.groupTokens))
            } else if (playingField.amountOfPlayers > 2) {
                playingField.copy(innerField34Players = playingField.innerField34Players.add(group.groupTokens))
            } else {
                playingField
            }
        }
        playingField = updatedPlayingField
        group.groupTokens
    }
}