package de.htwg.se.rummikub.controller

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.util.Observable
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.util.TokenUtils.tokensMatch
import de.htwg.se.rummikub.util.{Command, UndoManager}
import de.htwg.se.rummikub.util.commands.{AddRowCommand, AddGroupCommand}

import scala.io.StdIn.readLine

class Controller(var gameMode: GameModeTemplate) extends Observable {

    var playingField: Option[PlayingField] = None
    var validFirstMoveThisTurn: Boolean = false
    var gameState: Option[GameState] = None
    var currentPlayerIndex: Int = 0

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

    def createRow(r: List[Token]): Row = {
        Row(r)
    }

    def createGroup(g: List[Token]): Group = {
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
        val updatedPlayer = player.copy(tokens = player.tokens ++ tokensToAdd)

        playingField = playingField.map { field =>
            val updatedPlayers = field.players.map {
                case p if p.name == player.name => updatedPlayer
                case p => p
            }
            field.copy(players = updatedPlayers)
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
        playingField match {
            case Some(field) =>
                val current = field.players.indexWhere(_.name == p.name)
                val nextIndex = if (current == -1 || field.players.isEmpty) 0
                                else if (current == field.players.size - 1) 0
                                else current + 1

                currentPlayerIndex = nextIndex
                field.players(nextIndex)
            case None =>
                p
        }
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
            val remainingTokens = currentPlayer.tokens.filterNot(playerToken =>
                row.tokens.exists(tokenInRow => tokensMatch(tokenInRow, playerToken))
            )

            val updatedPlayer = currentPlayer.copy(tokens = remainingTokens, commandHistory = currentPlayer.commandHistory :+ s"row:${row.tokens.mkString(",")}").addToFirstMoveTokens(row.tokens)

            playingField = playingField.map { field =>
                val updatedPlayers = field.players.map {
                    case p if p.name == currentPlayer.name => updatedPlayer
                    case p => p
                }
                field.copy(innerField = field.innerField.add(row.tokens), players = updatedPlayers)
            }
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
            val remainingTokens = currentPlayer.tokens.filterNot(playerToken =>
                group.tokens.exists(tokenInGroup => tokensMatch(tokenInGroup, playerToken))
            )

            val updatedPlayer = currentPlayer.copy(tokens = remainingTokens, commandHistory = currentPlayer.commandHistory :+ s"group:${group.tokens.mkString(",")}").addToFirstMoveTokens(group.tokens)

            playingField = playingField.map { field =>
                val updatedPlayers = field.players.map {
                    case p if p.name == currentPlayer.name => updatedPlayer
                    case p => p
                }
                field.copy(innerField = field.innerField.add(group.tokens), players = updatedPlayers)
            }
            (group.tokens, updatedPlayer)
        }
    }

    def changeStringListToTokenList(list: List[String]): List[Token] = { 
        list.map { tokenString =>
            val tokenParts = tokenString.split(":")
            val tokenFactory = new StandardTokenFactory

            if (tokenParts(0) == "J") {
                tokenParts(1) match {
                    case "red" => tokenFactory.createJoker(Color.RED)
                    case "black" => tokenFactory.createJoker(Color.BLACK)
                }
            } else  {
                tokenParts(1) match {
                    case "red" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.RED)
                    case "blue" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.BLUE)
                    case "green" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.GREEN)
                    case "black" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.BLACK)
                }
            }
        }
    }


    def processGameInput(gameInput: String, currentPlayer: Player, stack: TokenStack): Player = {
        gameInput match {
            case "draw" => {
                undo()
                println("Drawing a token...")
                addTokenToPlayer(currentPlayer, stack)
                passTurn(currentPlayer, true)
            }

            case "pass" => passTurn(currentPlayer)

            case "row" =>
                println("Enter the tokens to play as row (e.g. 'token1:color, token2:color, ...'):")
                val tokens = readLine().split(",").map(_.trim).toList
                val row = createRow(changeStringListToTokenList(tokens))

                if (!row.isValid) {
                    println("It's not a valid row!")
                    currentPlayer
                } else {
                    executeAddRow(row, currentPlayer, stack)

                    val updatedPlayer = getUpdatedPlayerAfterMove(getState.currentPlayer, row.tokens)
                    if (!updatedPlayer.validateFirstMove()) {
                        println("Your move is not valid for the first move requirement.")
                    } else {
                        validFirstMoveThisTurn = true
                    }

                    updatedPlayer
                }

            case "group" =>
                println("Enter the tokens to play as group (e.g. 'token1:color, token2:color, ...'):")
                val tokens = readLine().split(",").map(_.trim).toList
                val group = createGroup(changeStringListToTokenList(tokens))

                if (!group.isValid) {
                    println("It's not a valid group!")
                    currentPlayer
                } else {

                    executeAddGroup(group, currentPlayer, stack)

                    val updatedPlayer = getUpdatedPlayerAfterMove(getState.currentPlayer, group.tokens)
                    if (!updatedPlayer.validateFirstMove()) {
                        println("Your move is not valid for the first move requirement.")
                    } else {
                        validFirstMoveThisTurn = true
                    }

                    updatedPlayer
                }

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

    def getState: GameState = playingField match {
        case Some(field) =>
            val copiedPlayers = field.players.map(_.deepCopy)
            val copiedBoards = field.boards.map(identity)

            GameState(
                table = field.innerField,
                players = copiedPlayers.toVector,
                boards = copiedBoards.toVector,
                currentPlayerIndex = currentPlayerIndex
            )
        case None => GameState(
                        table = Table(16, 90, List.empty),
                        players = Vector.empty,
                        boards = Vector.empty,
                        currentPlayerIndex = 0
                    )
    }

    def setStateInternal(state: GameState): Unit = {
        this.gameState = Some(state)
        this.playingField = Some(
            PlayingField(state.players.toList, state.boards.toList, state.table)
        )
        this.currentPlayerIndex = state.currentPlayerIndex
    }

    def executeAddRow(row: Row, player: Player, stack: TokenStack): Unit = {
        val cmd = new AddRowCommand(this, row, player, stack)
        undoManager.doStep(cmd)
    }

    def executeAddGroup(group: Group, player: Player, stack: TokenStack): Unit = {
        val cmd = new AddGroupCommand(this, group, player, stack)
        undoManager.doStep(cmd)
    }

    def undo(): Unit = undoManager.undoStep()
    def redo(): Unit = undoManager.redoStep()

    private def getUpdatedPlayerAfterMove(currentPlayer: Player, newTokens: List[Token]): Player = {
        val updatedPlayer = currentPlayer.addToFirstMoveTokens(newTokens)

        playingField = playingField.map { field =>
            val updatedPlayers = field.players.map {
            case p if p.name == updatedPlayer.name => updatedPlayer
            case p => p
            }
            field.copy(players = updatedPlayers)
        }
        updatedPlayer
    }
}