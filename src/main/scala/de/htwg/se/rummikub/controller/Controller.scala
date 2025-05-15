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

    def setPlayingField(pf: PlayingField): Unit = {
        this.playingField = pf
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
        val nextPlayer = setNextPlayer(currentPlayer).copy(commandHistory = List())
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

    def validateFirstMove(currentPlayer: Player): Boolean = {
        val totalPoints = currentPlayer.commandHistory.collect {
            case command if command.startsWith("row:") || command.startsWith("group:") =>
                val tokens = command.split(":")(1).split(",").map(_.trim)
                tokens.collect {
                    case tokenString if tokenString.contains(":") =>
                        val parts = tokenString.split(":")
                        if (parts(0).forall(_.isDigit)) parts(0).toInt else 0
                }.sum
        }.sum

        if (totalPoints < 30) {
            println("The first move must have a total of at least 30 points.")
            false
        } else {
            true
        }
    }

    def addRowToTable(row: Row, currentPlayer: Player): List[Token] = {
        val unmatched = row.rowTokens.filterNot(tokenInRow =>
          currentPlayer.tokens.exists(playerToken => tokensMatch(tokenInRow, playerToken))
        )
      
        if (unmatched.isEmpty) {
          playingField = playingField.copy(innerField = playingField.innerField.add(row.rowTokens))
          playingField = playingField.copy(players = playingField.players.map {
            case p if p.name == currentPlayer.name =>
              p.copy(commandHistory = p.commandHistory :+ s"row:${row.rowTokens.map(_.toString).mkString(",")}")
            case p => p
          })
          row.rowTokens
        } else {
          println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
          List.empty[Token]
        }
      }      

    def addGroupToTable(group: Group, currentPlayer: Player): List[Token] = {
        val unmatched = group.groupTokens.filterNot(tokenInGroup =>
          currentPlayer.tokens.exists(playerToken => tokensMatch(tokenInGroup, playerToken))
        )
      
        if (unmatched.isEmpty) {
          playingField = playingField.copy(innerField = playingField.innerField.add(group.groupTokens))
          playingField = playingField.copy(players = playingField.players.map {
            case p if p.name == currentPlayer.name =>
              p.copy(commandHistory = p.commandHistory :+ s"group:${group.groupTokens.map(_.toString).mkString(",")}")
            case p => p
          })
          group.groupTokens
        } else {
          println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
          List.empty[Token]
        }
    }

    def tokensMatch(token1: Token, token2: Token): Boolean = (token1, token2) match {
        case (NumToken(n1, c1), NumToken(n2, c2)) => n1 == n2 && c1 == c2
        case (_: Joker, _: Joker) => true
        case _ => false
    }

    def endTurn(currentPlayer: Player): Player = {
        if (currentPlayer.commandHistory.isEmpty) {
            println("You must play at least one token before ending your turn.")
            currentPlayer
        } else if (!currentPlayer.validateFirstMove()) {
            println("The first move must have a total of at least 30 points. You cannot end your turn.")
            currentPlayer
        } else {
            val nextPlayer = passTurn(currentPlayer)
            println(s"${currentPlayer.name} ended their turn. It's now ${nextPlayer.name}'s turn.")
            nextPlayer
        }
    }

    def processGameInput(gameInput: String, currentPlayer: Player, stack: TokenStack): Player = {
        gameInput match {
          case "draw" => {
            if (currentPlayer.validateFirstMove()) {
                println("You cannot draw a token after making a valid first move.")
                currentPlayer
              } else {
                println("Drawing a token...")
                addTokenToPlayer(currentPlayer, stack)
                passTurn(currentPlayer)
              }
            }

            case "pass" =>
                if (currentPlayer.commandHistory.isEmpty || !currentPlayer.validateFirstMove()) {
                    println("You cannot pass your turn without making a valid first move of at least 30 points.")
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

            case "end" =>
                endTurn(currentPlayer)

            case _ =>
                println("Invalid command.")
                currentPlayer
        }
    }
}