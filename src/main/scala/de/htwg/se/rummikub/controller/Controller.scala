package de.htwg.se.rummikub.controller

import de.htwg.se.rummikub.model.{Player, PlayingField, TokenStack, Row, Group, Token, Joker}
import de.htwg.se.rummikub.util.Observable

class Controller(var playingField: PlayingField) extends Observable {
    def createPlayingField(amountOfPlayers: Int, players: List[Player]): Unit = {
        playingField = PlayingField(amountOfPlayers, players)
        notifyObservers
    }

    def getPlayingField: PlayingField = playingField

    def setPlayingField(playingField: PlayingField): Unit = {
        this.playingField = playingField
        notifyObservers
    }

    def playingfieldToString: String = {
        playingField.toString()
    }

    def addTokenToPlayer(player: Player, token: Token | Joker): Unit = {
        playingField = playingField.copy(players = playingField.players.map {
            case p if p.name == player.name => p.copy(tokens = p.tokens :+ token)
            case p => p
        })
        notifyObservers
    }

    def removeTokenFromPlayer(player: Player, token: Token | Joker): Unit = {
        playingField = playingField.copy(players = playingField.players.map {
            case p if p.name == player.name => p.copy(tokens = p.tokens.filterNot(_.equals(token)))
            case p => p
        })
        notifyObservers
    }
    
    def addPlayer(name: String): Unit = {
        val player = Player(name)
        playingField = playingField.copy(players = playingField.players :+ player)
        notifyObservers
    }

}