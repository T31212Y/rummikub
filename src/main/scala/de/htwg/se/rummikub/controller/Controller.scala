package de.htwg.se.rummikub.controller

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.util.TokenUtils.tokensMatch
import de.htwg.se.rummikub.util.{Command, UndoManager}
import de.htwg.se.rummikub.util.commands.{AddRowCommand, AddGroupCommand, AppendTokenCommand}

import scala.swing.Publisher

class Controller(var gameMode: GameModeTemplate) extends Publisher {

    var playingField: Option[PlayingField] = None
    var gameState: Option[GameState] = None
    var currentPlayerIndex: Int = 0
    var turnStartState: Option[GameState] = None

    var turnUndoManager: UndoManager = new UndoManager
    var gameEnded: Boolean = false

    def setupNewGame(amountPlayers: Int, names: List[String]): Unit = {
        gameMode = GameModeFactory.createGameMode(amountPlayers, names).get
        playingField = gameMode.runGameSetup()

        gameState = playingField.map { field =>
            GameState(field.innerField, field.players.toVector, field.boards.toVector, 0, field.stack)
        }
        gameEnded = false
        publish(UpdateEvent())
    }

    def startGame(): Unit = {
        val stack = getState.stack
        val (updatedPlayers, updatedStack) = gameState.get.players.foldLeft((Vector.empty[Player], stack)) {
            case ((playersAcc, stackAcc), player) =>
            val (updatedPlayer, newStack) = addMultipleTokensToPlayer(player, stackAcc, 14)
            (playersAcc :+ updatedPlayer, newStack)
        }

        val newState = gameState.get.copy(players = updatedPlayers, stack = updatedStack)
        setStateInternal(newState)
        publish(UpdateEvent())
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
        publish(UpdateEvent())
    }

    def playingfieldToString: String = {
        gameMode.renderPlayingField(playingField)
    }

    def addTokenToPlayer(player: Player, stack: TokenStack): (Player, TokenStack) = {
        val (token, updatedStack) = stack.drawToken()
        val updatedPlayer = player.copy(tokens = player.tokens :+ token)

        (updatedPlayer, updatedStack)
    }

    def removeTokenFromPlayer(player: Player, token: Token): Unit = {
        playingField = playingField.map { field =>
            field.copy(players = field.players.map {
                case p if p.name == player.name => p.copy(tokens = p.tokens.filterNot(_.equals(token)))
                case p => p
            })
        }
    }

    def addMultipleTokensToPlayer(player: Player, stack: TokenStack, amt: Int): (Player, TokenStack) = {
        val (tokensToAdd, updatedStack) = stack.drawMultipleTokens(amt)
        val updatedPlayer = player.copy(tokens = player.tokens ++ tokensToAdd)

        (updatedPlayer, updatedStack)
    }
    
    def passTurn(state: GameState, ignoreFirstMoveCheck: Boolean = false): (GameState, String) = {
        val currentPlayer = state.currentPlayer

        if (!ignoreFirstMoveCheck && !currentPlayer.hasCompletedFirstMove) {
            val message = "The first move must have a total of at least 30 points. You cannot end your turn."
            (state, message)
        } else {
            val nextState = setNextPlayer(state)
            turnStartState = None
            val message = s"${state.currentPlayer.name} ended their turn. It's now ${nextState.currentPlayer.name}'s turn."

            setStateInternal(nextState)
            setPlayingField(gameMode.updatePlayingField(playingField))
            publish(UpdateEvent())

            (nextState, message)
        }
    }

    def setNextPlayer(state: GameState): GameState = {
        val current = state.currentPlayerIndex
        val nextIndex = if (state.players.isEmpty) 0
                        else (current + 1) % state.players.size

        val cleared = state.players(nextIndex).copy(commandHistory = List())
        state.updatePlayerIndex(nextIndex).updateCurrentPlayer(cleared)
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
            val updatedTable = playingField.get.innerField.add(row.tokens)

            val updatedPlayers = playingField.get.players.map {
                    case p if p.name == currentPlayer.name => updatedPlayer
                    case p => p
            }

            val newState = getState.updateCurrentPlayer(updatedPlayer).updateTable(updatedTable).updatePlayers(updatedPlayers.toVector)

            setStateInternal(newState)

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
            val updatedTable = playingField.get.innerField.add(group.tokens)

            val updatedPlayers = playingField.get.players.map {
                    case p if p.name == currentPlayer.name => updatedPlayer
                    case p => p
            }

            val newState = getState.updateCurrentPlayer(updatedPlayer).updateTable(updatedTable).updatePlayers(updatedPlayers.toVector)

            setStateInternal(newState)

            (group.tokens, updatedPlayer)
        }
    }

    def changeStringListToTokenList(list: List[String]): List[Token] = { 
        list.map { tokenString =>
            val tokenParts = tokenString.split(":")
            if (tokenParts.length < 2)
                throw new IllegalArgumentException("Invalid token input.")
            val tokenFactory = new StandardTokenFactory

            if (tokenParts(0) == "J") {
                tokenParts(1) match {
                    case "red" => tokenFactory.createJoker(Color.RED)
                    case "black" => tokenFactory.createJoker(Color.BLACK)
                    case _ => {
                        throw new IllegalArgumentException(s"Invalid joker color: ${tokenParts(1)}")
                    }
                }
            } else  {
                tokenParts(1) match {
                    case "red" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.RED)
                    case "blue" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.BLUE)
                    case "green" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.GREEN)
                    case "black" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.BLACK)
                    case _ => {
                        throw new IllegalArgumentException(s"Invalid token color: ${tokenParts(1)}")
                    }
                }
            }
        }
    }

    def beginTurn(currentPlayer: Player): Unit = {
        if (currentPlayer.commandHistory.isEmpty) {
            turnStartState = Some(getState)
            turnUndoManager = new UndoManager
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
                currentPlayerIndex = currentPlayerIndex,
                stack = field.stack
            )
        case None => GameState(
                        table = Table(16, 90, List.empty),
                        players = Vector.empty,
                        boards = Vector.empty,
                        currentPlayerIndex = 0,
                        stack = createTokenStack()
                    )
    }

    def setStateInternal(state: GameState): Unit = {
        this.gameState = Some(state)
        this.playingField = Some(
            PlayingField(state.players.toList, state.boards.toList, state.table, state.stack)
        )
        this.currentPlayerIndex = state.currentPlayerIndex
    }

    def executeAddRow(row: Row, player: Player, stack: TokenStack): Unit = {
        val cmd = new AddRowCommand(this, row, player, stack)
        turnUndoManager.doStep(cmd)
    }

    def executeAddGroup(group: Group, player: Player, stack: TokenStack): Unit = {
      if (!getState.players.exists(_.name == player.name))
        throw new NoSuchElementException(player.name)
      val cmd = new AddGroupCommand(this, group, player, stack)
      turnUndoManager.doStep(cmd)
    }

    def executeAppendToRow(token: Token, rowIndex: Int, player: Player): Unit = {
        val cmd = new AppendTokenCommand(this, token, rowIndex, isRow = true, player)
        turnUndoManager.doStep(cmd)
    }

    def executeAppendToGroup(token: Token, groupIndex: Int, player: Player): Unit = {
        val cmd = new AppendTokenCommand(this, token, groupIndex, isRow = false, player)
        turnUndoManager.doStep(cmd)
    }

    def undo(): Unit = {
        turnUndoManager.undoStep()
        publish(UpdateEvent())
    }
    def redo(): Unit = {
        turnUndoManager.redoStep()
        publish(UpdateEvent())
    } 

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

    def drawFromStackAndPass: (GameState, String) = {
        turnStartState match {
            case Some(previousState) =>
            setStateInternal(previousState)
            case None =>
        }

        val currentPlayer = getState.currentPlayer
        val (updatedPlayer, updatedStack) = addTokenToPlayer(currentPlayer, getState.currentStack)

        val updatedState = getState.updateCurrentPlayer(updatedPlayer).updateStack(updatedStack)
        setStateInternal(updatedState)

        val (finalState, message) = passTurn(updatedState, true)

        setStateInternal(finalState)
        setPlayingField(gameMode.updatePlayingField(playingField))
        (finalState, message)
    }

    def playRow(tokenStrings: List[String], currentPlayer: Player, stack: TokenStack): (Player, String) = {
        val tokens = changeStringListToTokenList(tokenStrings)

        if (tokens.isEmpty)
            return (currentPlayer, "No valid tokens detected.")

        val row = createRow(tokens)

        if (!row.isValid)
            return (currentPlayer, "It's not a valid row!")

        executeAddRow(row, currentPlayer, stack)

        val updatedPlayer = getUpdatedPlayerAfterMove(getState.currentPlayer, row.tokens)

        if (!updatedPlayer.validateFirstMove())
            return (updatedPlayer, "Your move is not valid for the first move requirement.")

        val updatedPlayerWithFlag = updatedPlayer.copy(hasCompletedFirstMove = true)

        val newState = getState.updateCurrentPlayer(updatedPlayerWithFlag)

        setStateInternal(newState)
        setPlayingField(gameMode.updatePlayingField(playingField))

        (updatedPlayerWithFlag, "Row successfully placed.")
    }

    def playGroup(tokenStrings: List[String], currentPlayer: Player, stack: TokenStack): (Player, String) = {
        val tokens = changeStringListToTokenList(tokenStrings)

        if (tokens.isEmpty)
            return (currentPlayer, "No valid tokens detected.")

        val group = createGroup(tokens)

        if (!group.isValid)
            return (currentPlayer, "Your move is not valid for the first move requirement.")

        executeAddGroup(group, currentPlayer, stack)

        val updatedPlayer = getUpdatedPlayerAfterMove(getState.currentPlayer, group.tokens)

        if (!updatedPlayer.validateFirstMove())
            return (updatedPlayer, "Your move is not valid for the first move requirement.")

        val updatedPlayerWithFlag = updatedPlayer.copy(hasCompletedFirstMove = true)

        val newState = getState.updateCurrentPlayer(updatedPlayerWithFlag)

        setStateInternal(newState)
        setPlayingField(gameMode.updatePlayingField(playingField))

        (updatedPlayerWithFlag, "Group successfully placed.")
    }

    def appendTokenToRow(tokenString: String, index: Int): (Player, String) = {
        val tokenList = changeStringListToTokenList(List(tokenString))

        if (tokenList.isEmpty)
            return (getState.currentPlayer, "Invalid token input.")

        val token = tokenList.head
        val currentPlayer = getState.currentPlayer

        executeAppendToRow(token, index, currentPlayer)

        val updatedPlayer = getUpdatedPlayerAfterMove(getState.currentPlayer, List(token))

        val newState = getState.updateCurrentPlayer(updatedPlayer)

        setStateInternal(newState)
        setPlayingField(gameMode.updatePlayingField(playingField))

        (updatedPlayer, s"Token appended to row at index $index.")
    }

    def appendTokenToGroup(tokenString: String, index: Int): (Player, String) = {
        val tokenList = changeStringListToTokenList(List(tokenString))

        if (tokenList.isEmpty)
            return (getState.currentPlayer, "Invalid token input.")

        val token = tokenList.head
        val currentPlayer = getState.currentPlayer

        executeAppendToGroup(token, index, currentPlayer)

        val updatedPlayer = getUpdatedPlayerAfterMove(getState.currentPlayer, List(token))

        val newState = getState.updateCurrentPlayer(updatedPlayer)

        setStateInternal(newState)
        setPlayingField(gameMode.updatePlayingField(playingField))

        (updatedPlayer, s"Token appended to group at index $index.")
    }
    
    def endGame(): Unit = {
        gameEnded = true
        publish(UpdateEvent())
    }
}