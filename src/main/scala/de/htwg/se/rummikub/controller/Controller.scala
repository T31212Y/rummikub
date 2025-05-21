package de.htwg.se.rummikub.controller

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.util.Observable
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.util.TokenUtils.tokensMatch
import de.htwg.se.rummikub.util.{Command, UndoManager}

import scala.io.StdIn.readLine
import de.htwg.se.rummikub.util.commands.AddRowCommand

class Controller(var gameMode: GameModeTemplate) extends Observable {

    var playingField: Option[PlayingField] = None
    var validFirstMoveThisTurn: Boolean = false
    var gameState: Option[GameState] = None

    val undoManager = new UndoManager

    def setupNewGame(amountPlayers: Int, names: List[String]): Unit = {
        gameMode = GameModeFactory.createGameMode(amountPlayers, names).get
        playingField = gameMode.runGameSetup()

        gameState = playingField.map { field =>
            GameState(field.innerField, field.players.toVector, field.boards.toVector, 0)
        }

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

    def setPlayingField(pf: Option[PlayingField]): Unit = {
        this.playingField = pf
        notifyObservers
    }

    def playingfieldToString: String = {
        gameMode.renderPlayingField(playingField)
    }

    def addTokenToPlayer(player: Player, stack: TokenStack): Unit = {
        val tokenToAdd = stack.drawToken()
        playingField = playingField.map { field =>
            field.copy(players = field.players.map {
                case p if p.name == player.name => p.copy(tokens = p.tokens :+ tokenToAdd)
                case p => p
            })
        }
    }

    def removeTokenFromPlayer(player: Player, token: Token): Unit = {
        playingField = playingField.map { field =>
            field.copy(players = field.players.map {
                case p if p.name == player.name => p.copy(tokens = p.tokens.filterNot(_.equals(token)))
                case p => p
            })
        }
    }

    def addMultipleTokensToPlayer(player: Player, stack: TokenStack, amt: Int): Unit = {
        val tokensToAdd = stack.drawMultipleTokens(amt)
        playingField = playingField.map { field =>
            field.copy(players = field.players.map {
                case p if p.name == player.name => p.copy(tokens = p.tokens ++ tokensToAdd)
                case p => p
            })
        }
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
        playingField.flatMap(field => {
            val current = field.players.indexWhere(_.name == p.name)
            if (current == -1 || field.players.isEmpty) None
            else if (current == field.players.size - 1) Some(field.players.head)
            else Some(field.players(current + 1))
        }).getOrElse(p)
    }

    def winGame(): Boolean = {
        playingField match {
            case Some(field) =>
                field.players.find(_.tokens.isEmpty) match {
                    case Some(winner) =>
                        println(s"Player ${winner.name} wins the game!")
                        true
                    case None => false
                }
            case None => false
        }
    }

    def addRowToTable(row: Row, currentPlayer: Player): (List[Token], Player) = {
        val unmatched = row.tokens.filterNot(tokenInRow =>
          currentPlayer.tokens.exists(playerToken => tokensMatch(tokenInRow, playerToken))
        )
      
        if (unmatched.nonEmpty) {
            println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
            (List.empty, currentPlayer)
        } else {
            val updatedPlayer = currentPlayer.copy(commandHistory = currentPlayer.commandHistory :+ s"row:${row.tokens.mkString(",")}").addToFirstMoveTokens(row.tokens)
        
            playingField = playingField.map( field =>
                field.copy(innerField = field.innerField.add(row.tokens), players = field.players.map(p =>
                    if (p.name == currentPlayer.name) updatedPlayer else p
                ))
            )
            (row.tokens, updatedPlayer)
        }
    }

    def addGroupToTable(group: Group, currentPlayer: Player): (List[Token], Player) = {
        val unmatched = group.tokens.filterNot(tokenInGroup =>
          currentPlayer.tokens.exists(playerToken => tokensMatch(tokenInGroup, playerToken))
        )

        if (unmatched.nonEmpty) {
            println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
            (List.empty, currentPlayer)
        } else {
            val updatedPlayer = currentPlayer.copy(commandHistory = currentPlayer.commandHistory :+ s"group:${group.tokens.mkString(",")}").addToFirstMoveTokens(group.tokens)

            playingField = playingField.map( field =>
                field.copy(innerField = field.innerField.add(group.tokens), players = field.players.map(p =>
                    if (p.name == currentPlayer.name) updatedPlayer else p
                ))
            )
            (group.tokens, updatedPlayer)
        }
    }


    def processGameInput(gameInput: String, currentPlayer: Player, stack: TokenStack): Player = {
        gameInput match {
          case "draw" => {
            if (validFirstMoveThisTurn) {
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
                val row = createRow(tokens)

                executeAddRow(row, currentPlayer, stack)

                val updatedPlayer = getState.currentPlayer
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

            case "undo" =>
                undo()
                currentPlayer

            case "redo" =>
                redo()
                currentPlayer

            case "end" =>
                println("Exiting the game...")
                currentPlayer

            case _ =>
                println("Invalid command.")
                currentPlayer
        }
    }

    def getState: GameState = gameState.getOrElse(
        GameState(Table(6, 30), Vector.empty, Vector.empty, 0)
    )

    def setStateInternal(state: GameState): Unit = {
        this.gameState = Some(state)
        this.playingField = Some(
            PlayingField(state.players.toList, state.boards.toList, state.table)
        )
        notifyObservers
    }

    def executeAddRow(row: Row, player: Player, stack: TokenStack): Unit = {
        val cmd = new AddRowCommand(this, row, player, stack)
        undoManager.doStep(cmd)
        notifyObservers
    }

    def undo(): Unit = undoManager.undoStep()
    def redo(): Unit = undoManager.redoStep()
}