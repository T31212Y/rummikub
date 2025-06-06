package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.rummikub.util.TokenUtils.tokensMatch
import de.htwg.se.rummikub.util.{Command, UndoManager}

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}
import de.htwg.se.rummikub.model.playingFieldComponent.{TokenStackInterface, PlayingFieldInterface}
import de.htwg.se.rummikub.model.gameModeComponent.{GameModeTemplate, GameModeFactoryInterface}
import de.htwg.se.rummikub.controller.controllerComponent.{ControllerInterface, UpdateEvent, GameStateInterface}

import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.StandardTokenFactory
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.{Row, Group}
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.{TokenStack, Table, PlayingField}

import scala.swing.Publisher

class Controller(var gameMode: GameModeTemplate, val gameModeFactory: GameModeFactoryInterface) extends ControllerInterface with Publisher {

    var playingField: Option[PlayingFieldInterface] = None
    var gameState: Option[GameStateInterface] = None
    var currentPlayerIndex: Int = 0
    var turnStartState: Option[GameState] = None

    var turnUndoManager: UndoManager = new UndoManager
    var gameEnded: Boolean = false

    override def setupNewGame(amountPlayers: Int, names: List[String]): Unit = {
        gameMode = gameModeFactory.createGameMode(amountPlayers, names).get
        playingField = gameMode.runGameSetup

        gameState = playingField.map { field =>
            GameState(field.getInnerField, field.getPlayers.toVector, field.getBoards.toVector, 0, field.getStack)
        }
        gameEnded = false
        publish(UpdateEvent())
    }

    override def startGame: Unit = {
        val stack = getState.stack
        val (updatedPlayers, updatedStack) = gameState.get.getPlayers.foldLeft((Vector.empty[PlayerInterface], stack)) {
            case ((playersAcc, stackAcc), player) =>
            val (updatedPlayer, newStack) = addMultipleTokensToPlayer(player, stackAcc, 14)
            (playersAcc :+ updatedPlayer, newStack)
        }

        val newState = gameState.get.updated(newPlayers = updatedPlayers, newStack = updatedStack)
        setStateInternal(newState)
        publish(UpdateEvent())
    }

    override def createTokenStack: TokenStackInterface = {
        TokenStack()
    }

    override def createRow(r: List[TokenInterface]): Row = {
        Row(r)
    }

    override def createGroup(g: List[TokenInterface]): Group = {
        Group(g)
    }

    override def setPlayingField(pf: Option[PlayingFieldInterface]): Unit = {
        this.playingField = pf
        publish(UpdateEvent())
    }

    override def playingFieldToString: String = {
        gameMode.renderPlayingField(playingField)
    }

    override def addTokenToPlayer(player: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, TokenStackInterface) = {
        val (token, updatedStack) = stack.drawToken
        val updatedPlayer = player.updated(newTokens = player.getTokens :+ token, newCommandHistory = player.getCommandHistory, newHasCompletedFirstMove = player.getHasCompletedFirstMove)

        (updatedPlayer, updatedStack)
    }

    override def removeTokenFromPlayer(player: PlayerInterface, token: TokenInterface): Unit = {
        playingField = playingField.map { field =>
            field.updated(newPlayers = field.getPlayers.map {
                case p if p.getName == player.getName => p.updated(newTokens = p.getTokens.filterNot(_.equals(token)), newCommandHistory = p.getCommandHistory, newHasCompletedFirstMove = p.getHasCompletedFirstMove)
                case p => p
            }, newBoards = field.getBoards, newInnerField = field.getInnerField)
        }
    }

    override def addMultipleTokensToPlayer(player: PlayerInterface, stack: TokenStackInterface, amt: Int): (PlayerInterface, TokenStackInterface) = {
        val (tokensToAdd, updatedStack) = stack.drawMultipleTokens(amt)
        val updatedPlayer = player.updated(newTokens = player.getTokens ++ tokensToAdd, newCommandHistory = player.getCommandHistory, newHasCompletedFirstMove = player.getHasCompletedFirstMove)

        (updatedPlayer, updatedStack)
    }
    
    override def passTurn(state: GameStateInterface, ignoreFirstMoveCheck: Boolean = false): (GameStateInterface, String) = {
        val currentPlayer = state.currentPlayer

        if (!ignoreFirstMoveCheck && !currentPlayer.getHasCompletedFirstMove) {
            val message = "The first move must have a total of at least 30 points. You cannot end your turn."
            (state, message)
        } else {
            val nextState = setNextPlayer(state)
            turnStartState = None
            val message = s"${state.currentPlayer.getName} ended their turn. It's now ${nextState.currentPlayer.getName}'s turn."

            setStateInternal(nextState)
            setPlayingField(gameMode.updatePlayingField(playingField))
            publish(UpdateEvent())

            (nextState, message)
        }
    }

    override def setNextPlayer(state: GameStateInterface): GameStateInterface = {
        val current = state.getCurrentPlayerIndex
        val nextIndex = (current + 1) % state.getPlayers.size

        val cleared = state.getPlayers(nextIndex).updated(newTokens = state.getPlayers(nextIndex).getTokens, newCommandHistory = List(), newHasCompletedFirstMove = state.getPlayers(nextIndex).getHasCompletedFirstMove)
        state.updatePlayerIndex(nextIndex).updateCurrentPlayer(cleared)
    }

    override def winGame: Boolean = {
        playingField match {
            case Some(field) =>
                field.getPlayers.find(_.getTokens.isEmpty) match {
                    case Some(winner) =>
                        println(s"Player ${winner.getName} wins the game!")
                        true
                    case None => false
                }
            case None => false
        }
    }

    override def addRowToTable(row: Row, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface) = {
        val unmatched = row.tokens.filterNot(tokenInRow =>
          currentPlayer.getTokens.exists(playerToken => tokensMatch(tokenInRow, playerToken))
        )
      
        if (unmatched.nonEmpty) {
            println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
            (List.empty, currentPlayer)
        } else {
            val remainingTokens = currentPlayer.getTokens.filterNot(playerToken =>
                row.tokens.exists(tokenInRow => tokensMatch(tokenInRow, playerToken))
            )

            val updatedPlayer = currentPlayer.updated(newTokens = remainingTokens, newCommandHistory = currentPlayer.getCommandHistory :+ s"row:${row.tokens.mkString(",")}", newHasCompletedFirstMove = currentPlayer.getHasCompletedFirstMove).addToFirstMoveTokens(row.tokens)
            val updatedTable = playingField.get.getInnerField.add(row.tokens)

            val updatedPlayers = playingField.get.getPlayers.map {
                    case p if p.getName == currentPlayer.getName => updatedPlayer
                    case p => p
            }

            val newState = getState.updateCurrentPlayer(updatedPlayer).updateTable(updatedTable).updatePlayers(updatedPlayers.toVector)

            setStateInternal(newState)

            (row.tokens, updatedPlayer)
        }
    }

    override def addGroupToTable(group: Group, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface) = {
        val unmatched = group.tokens.filterNot(tokenInGroup =>
          currentPlayer.getTokens.exists(playerToken => tokensMatch(tokenInGroup, playerToken))
        )

        if (unmatched.nonEmpty) {
            println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
            (List.empty, currentPlayer)
        } else {
            val remainingTokens = currentPlayer.getTokens.filterNot(playerToken =>
                group.tokens.exists(tokenInGroup => tokensMatch(tokenInGroup, playerToken))
            )

            val updatedPlayer = currentPlayer.updated(newTokens = remainingTokens, newCommandHistory = currentPlayer.getCommandHistory :+ s"group:${group.tokens.mkString(",")}", newHasCompletedFirstMove = currentPlayer.getHasCompletedFirstMove).addToFirstMoveTokens(group.tokens)
            val updatedTable = playingField.get.getInnerField.add(group.tokens)

            val updatedPlayers = playingField.get.getPlayers.map {
                    case p if p.getName == currentPlayer.getName => updatedPlayer
                    case p => p
            }

            val newState = getState.updateCurrentPlayer(updatedPlayer).updateTable(updatedTable).updatePlayers(updatedPlayers.toVector)

            setStateInternal(newState)

            (group.tokens, updatedPlayer)
        }
    }

    override def changeStringListToTokenList(list: List[String]): List[TokenInterface] = { 
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

    override def beginTurn(currentPlayer: PlayerInterface): Unit = {
        if (currentPlayer.getCommandHistory.isEmpty) {
            turnStartState = Some(getState)
            turnUndoManager = new UndoManager
        }
    }

    override def getState: GameState = playingField match {
        case Some(field) =>
            val copiedPlayers = field.getPlayers.map(_.deepCopy)
            val copiedBoards = field.getBoards.map(identity)

            GameState(
                table = field.getInnerField,
                players = copiedPlayers.toVector,
                boards = copiedBoards.toVector,
                currentPlayerIndex = currentPlayerIndex,
                stack = field.getStack
            )
        case None => GameState(
                        table = Table(16, 90, List.empty),
                        players = Vector.empty,
                        boards = Vector.empty,
                        currentPlayerIndex = 0,
                        stack = createTokenStack
                    )
    }

    override def setStateInternal(state: GameStateInterface): Unit = {
        this.gameState = Some(state)
        this.playingField = Some(
            PlayingField(state.getPlayers.toList, state.getBoards.toList, state.getTable, state.currentStack)
        )
        this.currentPlayerIndex = state.getCurrentPlayerIndex
    }

    override def executeAddRow(row: Row, player: PlayerInterface, stack: TokenStackInterface): Unit = {
        val cmd = new AddRowCommand(this, row, player, stack)
        turnUndoManager.doStep(cmd)
    }

    override def executeAddGroup(group: Group, player: PlayerInterface, stack: TokenStackInterface): Unit = {
      if (!getState.players.exists(_.getName == player.getName))
        throw new NoSuchElementException(player.getName)
      val cmd = new AddGroupCommand(this, group, player, stack)
      turnUndoManager.doStep(cmd)
    }

    override def executeAppendToRow(token: TokenInterface, rowIndex: Int, player: PlayerInterface): Unit = {
        val cmd = new AppendTokenCommand(this, token, rowIndex, isRow = true, player)
        turnUndoManager.doStep(cmd)
    }

    override def executeAppendToGroup(token: TokenInterface, groupIndex: Int, player: PlayerInterface): Unit = {
        val cmd = new AppendTokenCommand(this, token, groupIndex, isRow = false, player)
        turnUndoManager.doStep(cmd)
    }

    override def undo: Unit = {
        turnUndoManager.undoStep()
        publish(UpdateEvent())
    }

    override def redo: Unit = {
        turnUndoManager.redoStep()
        publish(UpdateEvent())
    } 

    private def getUpdatedPlayerAfterMove(currentPlayer: PlayerInterface, newTokens: List[TokenInterface]): PlayerInterface = {
        val updatedPlayer = currentPlayer.addToFirstMoveTokens(newTokens)

        playingField = playingField.map { field =>
            val updatedPlayers = field.getPlayers.map {
            case p if p.getName == updatedPlayer.getName => updatedPlayer
            case p => p
            }
            field.updated(newPlayers = updatedPlayers, newBoards = field.getBoards, newInnerField = field.getInnerField)
        }
        updatedPlayer
    }

    override def drawFromStackAndPass: (GameStateInterface, String) = {
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

    override def playRow(tokenStrings: List[String], currentPlayer: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, String) = {
        val tokens = changeStringListToTokenList(tokenStrings)

        val row = createRow(tokens)

        if (!row.isValid)
            return (currentPlayer, "Your move is not valid for the first move requirement.")

        executeAddRow(row, currentPlayer, stack)

        val updatedPlayer = getUpdatedPlayerAfterMove(getState.currentPlayer, row.tokens)

        val updatedPlayerWithFlag = updatedPlayer.updated(newTokens = updatedPlayer.getTokens, newCommandHistory = updatedPlayer.getCommandHistory, newHasCompletedFirstMove = true)

        val newState = getState.updateCurrentPlayer(updatedPlayerWithFlag)

        setStateInternal(newState)
        setPlayingField(gameMode.updatePlayingField(playingField))

        (updatedPlayerWithFlag, "Row successfully placed.")
    }

    override def playGroup(tokenStrings: List[String], currentPlayer: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, String) = {
        val tokens = changeStringListToTokenList(tokenStrings)

        val group = createGroup(tokens)

        if (!group.isValid)
            return (currentPlayer, "Your move is not valid for the first move requirement.")

        executeAddGroup(group, currentPlayer, stack)

        val updatedPlayer = getUpdatedPlayerAfterMove(getState.currentPlayer, group.tokens)

        val updatedPlayerWithFlag = updatedPlayer.updated(newTokens = updatedPlayer.getTokens, newCommandHistory = updatedPlayer.getCommandHistory, newHasCompletedFirstMove = true)

        val newState = getState.updateCurrentPlayer(updatedPlayerWithFlag)

        setStateInternal(newState)
        setPlayingField(gameMode.updatePlayingField(playingField))

        (updatedPlayerWithFlag, "Group successfully placed.")
    }

    override def appendTokenToRow(tokenString: String, index: Int): (PlayerInterface, String) = {
        val tokenList = changeStringListToTokenList(List(tokenString))

        val token = tokenList.head
        val currentPlayer = getState.currentPlayer

        executeAppendToRow(token, index, currentPlayer)

        val updatedPlayer = getUpdatedPlayerAfterMove(getState.currentPlayer, List(token))

        val newState = getState.updateCurrentPlayer(updatedPlayer)

        setStateInternal(newState)
        setPlayingField(gameMode.updatePlayingField(playingField))

        (updatedPlayer, s"Token appended to row at index $index.")
    }

    override def appendTokenToGroup(tokenString: String, index: Int): (PlayerInterface, String) = {
        val tokenList = changeStringListToTokenList(List(tokenString))

        val token = tokenList.head
        val currentPlayer = getState.currentPlayer

        executeAppendToGroup(token, index, currentPlayer)

        val updatedPlayer = getUpdatedPlayerAfterMove(getState.currentPlayer, List(token))

        val newState = getState.updateCurrentPlayer(updatedPlayer)

        setStateInternal(newState)
        setPlayingField(gameMode.updatePlayingField(playingField))

        (updatedPlayer, s"Token appended to group at index $index.")
    }
    
    override def endGame: Unit = {
        gameEnded = true
        publish(UpdateEvent())
    }

    override def getGameEnded: Boolean = gameEnded

    override def getGameMode: GameModeTemplate = gameMode

    override def getPlayingField: Option[PlayingFieldInterface] = playingField

    override def setGameEnded(nge: Boolean): Unit = {
        gameEnded = nge
    }
}