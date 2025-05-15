package de.htwg.se.rummikub.controller

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.util.Observable

import scala.io.StdIn.readLine
import scala.compiletime.uninitialized


class Controller(var gameMode: GameModeTemplate) extends Observable {

    var playingField: PlayingField = uninitialized
    private var validFirstMoveThisTurn: Boolean = false

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
    
    def passTurn(currentPlayer: Player, allowWithoutFirstMove: Boolean = false): Player = {
        if (!allowWithoutFirstMove && (currentPlayer.commandHistory.isEmpty || !currentPlayer.validateFirstMove())) {
            println("The first move must have a total of at least 30 points. You cannot end your turn.")
            currentPlayer
        } else {
            val nextPlayer = setNextPlayer(currentPlayer).copy(commandHistory = List())
            println(s"${currentPlayer.name} ended their turn. It's now ${nextPlayer.name}'s turn.")
            validFirstMoveThisTurn = false
            nextPlayer
        }
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

    def addRowToTable(row: Row, currentPlayer: Player): (List[Token], Player) = {
        val unmatched = row.rowTokens.filterNot(tokenInRow =>
          currentPlayer.tokens.exists(playerToken => tokensMatch(tokenInRow, playerToken))
        )
      
        if (unmatched.nonEmpty) {
            println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
            (List.empty, currentPlayer)
        } else {
            val updatedPlayer = currentPlayer.copy(commandHistory = currentPlayer.commandHistory :+ s"row:${row.rowTokens.mkString(",")}").addToFirstMoveTokens(row.rowTokens)
        
            playingField = playingField.copy(innerField = playingField.innerField.add(row.rowTokens), players = playingField.players.map(p =>
                                                                                                        if (p.name == currentPlayer.name) updatedPlayer else p
                                                                                                    ))
            (row.rowTokens, updatedPlayer)
        }
    }

    def addGroupToTable(group: Group, currentPlayer: Player): (List[Token], Player) = {
        val unmatched = group.groupTokens.filterNot(tokenInGroup =>
          currentPlayer.tokens.exists(playerToken => tokensMatch(tokenInGroup, playerToken))
        )

        if (unmatched.nonEmpty) {
            println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
            (List.empty, currentPlayer)
        } else {
            val updatedPlayer = currentPlayer.copy(commandHistory = currentPlayer.commandHistory :+ s"group:${group.groupTokens.mkString(",")}").addToFirstMoveTokens(group.groupTokens)

            playingField = playingField.copy(innerField = playingField.innerField.add(group.groupTokens), players = playingField.players.map(p =>
                                                                                                        if (p.name == currentPlayer.name) updatedPlayer else p
                                                                                                    ))
            (group.groupTokens, updatedPlayer)
        }
    }

    def tokensMatch(token1: Token, token2: Token): Boolean = (token1, token2) match {
        case (NumToken(n1, c1), NumToken(n2, c2)) => n1 == n2 && c1 == c2
        case (_: Joker, _: Joker) => true
        case _ => false
    }

    def processGameInput(gameInput: String, currentPlayer: Player, stack: TokenStack): Player = {
        gameInput match {
          case "draw" => {
            if (validFirstMoveThisTurn || currentPlayer.validateFirstMove()) {
                println("You cannot draw a token after making a valid first move.")
                currentPlayer
              } else {
                println("Drawing a token...")
                addTokenToPlayer(currentPlayer, stack)
                passTurn(currentPlayer, true)
              }
            }

            case "pass" => passTurn(currentPlayer)

            case "row" =>
                println("Enter the tokens to play as row (e.g. 'token1:color, token2:color, ...'):")
                val tokens = readLine().split(",").map(_.trim).toList
                val (removeTokens, updatedPlayer) = addRowToTable(createRow(tokens), currentPlayer)
                removeTokens.foreach(t => removeTokenFromPlayer(updatedPlayer, t))

                if (!updatedPlayer.validateFirstMove()) {
                    println("Your move is not valid for the first move requirement.")
                } else {
                    validFirstMoveThisTurn = true
                }

                updatedPlayer

            case "group" =>
                println("Enter the tokens to play as group (e.g. 'token1:color, token2:color, ...'):")
                val tokens = readLine().split(",").map(_.trim).toList
                val (removeTokens, updatedPlayer) = addGroupToTable(createGroup(tokens), currentPlayer)
                removeTokens.foreach(t => removeTokenFromPlayer(updatedPlayer, t))

                if (!updatedPlayer.validateFirstMove()) {
                    println("Your move is not valid for the first move requirement.")
                } else {
                    validFirstMoveThisTurn = true
                }

                updatedPlayer

            case "end" =>
                println("Exiting the game...")
                currentPlayer

            case _ =>
                println("Invalid command.")
                currentPlayer
        }
    }
}