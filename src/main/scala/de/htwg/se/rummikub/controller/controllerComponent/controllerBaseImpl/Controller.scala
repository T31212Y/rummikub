package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.util.TokenUtils.tokensMatch
import de.htwg.se.rummikub.util.UndoManager
import de.htwg.se.rummikub.model.playingfieldComponent.PlayingFieldInterface
import de.htwg.se.rummikub.util.CommandInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface
import de.htwg.se.rummikub.model.playingfieldComponent.{TokenStackInterface, TableInterface}
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenFactoryInterface
import de.htwg.se.rummikub.model.gameModeComponent.GameModeFactory
import de.htwg.se.rummikub.model.tokenComponent.Color


class Controller(var gameMode: GameModeTemplate) extends ControllerInterface {
    override var playingField: Option[PlayingFieldInterface] = None
    override var gameState: Option[GameState] = None
    var currentPlayerIndex: Int = 0
    var turnStartState: Option[GameState] = None

    var turnUndoManager: UndoManager = new UndoManager
    var gameEnded: Boolean = false

    def setupNewGame(amountPlayers: Int, names: List[String]): Unit = {
        gameMode = GameModeFactory.createGameMode(amountPlayers, names).get
        playingField = gameMode.runGameSetup()
        gameState = playingField.map { field =>
        GameState(
            field.getInnerField,
            field.getPlayers.toVector,
            field.getBoards.toVector,
            0,
            field.getStack
        )
        }
        gameEnded = false
    }

    def startGame(): Unit = {
        val stack = getState.stack
        val (updatedPlayers, updatedStack) = gameState.get.players.foldLeft((Vector.empty[PlayerInterface], stack)) {
        case ((playersAcc, stackAcc), player) =>
            val (updatedPlayer, newStack) = addMultipleTokensToPlayer(player, stackAcc, 14)
            (playersAcc :+ updatedPlayer, newStack)
        }
        val newState = gameState.get.copy(players = updatedPlayers, stack = updatedStack)
        setStateInternal(newState)
    }

    def setPlayingField(pf: Option[PlayingFieldInterface]): Unit = {
        this.playingField = pf
    }

    def playingfieldToString: String = {
        gameMode.renderPlayingField(playingField)
    }

    def addTokenToPlayer(player: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, TokenStackInterface) = {
        val (token, updatedStack) = stack.drawToken()
        val updatedPlayer = player.withTokens(player.getTokens :+ token)
        (updatedPlayer, updatedStack)
    }

    def removeTokenFromPlayer(player: PlayerInterface, token: TokenInterface): Unit = {
        playingField = playingField.map { field =>
        val updatedPlayers = field.getPlayers.map { p =>
            if (p.name == player.name)
            p.withTokens(p.getTokens.filterNot(_.equals(token)))
            else p
        }
        field.setPlayers(updatedPlayers)
        }
    }

    def addMultipleTokensToPlayer(
        player: PlayerInterface,
        stack: TokenStackInterface,
        amt: Int
    ): (PlayerInterface, TokenStackInterface) = {
        val (tokensToAdd, updatedStack) = stack.drawMultipleTokens(amt)
        val updatedPlayer = player.withTokens(player.getTokens ++ tokensToAdd)
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
        (nextState, message)
        }
    }

    def setNextPlayer(state: GameState): GameState = {
        val current = state.currentPlayerIndex
        val nextIndex = (current + 1) % state.players.size
        val cleared = state.players(nextIndex).clearCommandHistory
        state.updatePlayerIndex(nextIndex).updateCurrentPlayer(cleared)
    }

    def winGame(): Boolean = {
        playingField match {
        case Some(field) =>
            field.getPlayers.find(_.getTokens.isEmpty) match {
            case Some(winner) =>
                println(s"Player ${winner.name} wins the game!")
                true
            case None => false
            }
        case None => false
        }
    }

    def addRowToTable(row: TokenStructureInterface, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface) = {
        val playerTokensFlat: List[TokenInterface] = currentPlayer.getTokens.flatMap(_.getTokens)

        val unmatched = row.getTokens.filterNot { tokenInRow =>
        playerTokensFlat.exists(playerToken => tokensMatch(tokenInRow, playerToken))
        }

        if (unmatched.nonEmpty) {
        println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
        (List.empty, currentPlayer)
        } else {
        val remainingTokensFlat = playerTokensFlat.filterNot { playerToken =>
            row.getTokens.exists(tokenInRow => tokensMatch(tokenInRow, playerToken))
        }

        val updatedTokensStructures: List[TokenStructureInterface] =
            remainingTokensFlat.map(token => gameMode.tokenStructureFactory.createSingleTokenStructure(token))

        val updatedPlayerWithTokens = currentPlayer.withTokens(updatedTokensStructures)
        val updatedPlayer = updatedPlayerWithTokens.addToFirstMoveTokens(row.getTokens)

        val updatedTable = playingField.get.getInnerField.add(row.getTokens)

        val updatedPlayers = playingField.get.getPlayers.map {
            case p if p.name == currentPlayer.name => updatedPlayer
            case p => p
        }

        val newState = getState
            .updateCurrentPlayer(updatedPlayer)
            .updateTable(updatedTable)
            .updatePlayers(updatedPlayers.toVector)

        setStateInternal(newState)

        (row.getTokens, updatedPlayer)
        }
    }

    def addGroupToTable(group: TokenStructureInterface, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface) = {
        val playerTokensFlat: List[TokenInterface] = currentPlayer.getTokens.flatMap(_.getTokens)

        val unmatched = group.getTokens.filterNot { tokenInGroup =>
        playerTokensFlat.exists(playerToken => tokensMatch(tokenInGroup, playerToken))
        }

        if (unmatched.nonEmpty) {
        println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
        (List.empty, currentPlayer)
        } else {
        val remainingTokensFlat = playerTokensFlat.filterNot { playerToken =>
            group.getTokens.exists(tokenInGroup => tokensMatch(tokenInGroup, playerToken))
        }

        val updatedTokensStructures: List[TokenStructureInterface] =
            remainingTokensFlat.map(token => gameMode.tokenStructureFactory.createSingleTokenStructure(token))

        val updatedPlayerWithTokens = currentPlayer.withTokens(updatedTokensStructures)
        val updatedPlayer = updatedPlayerWithTokens.addToFirstMoveTokens(group.getTokens)

        val updatedTable = playingField.get.getInnerField.add(group.getTokens)

        val updatedPlayers = playingField.get.getPlayers.map {
            case p if p.name == currentPlayer.name => updatedPlayer
            case p => p
        }

        val newState = getState
            .updateCurrentPlayer(updatedPlayer)
            .updateTable(updatedTable)
            .updatePlayers(updatedPlayers.toVector)

        setStateInternal(newState)

        (group.getTokens, updatedPlayer)
        }
    }

    def changeStringListToTokenList(
        list: List[String],
        tokenFactory: TokenFactoryInterface
    ): List[TokenInterface] = {
        list.map { tokenString =>
        val tokenParts = tokenString.split(":")
        if (tokenParts.length < 2)
            throw new IllegalArgumentException(s"Invalid token input: $tokenString")

        val value = tokenParts(0)
        val color = tokenParts(1).toLowerCase

        if (value == "J") {
            color match {
            case "red"   => tokenFactory.createJoker(Color.RED)
            case "black" => tokenFactory.createJoker(Color.BLACK)
            case _       => throw new IllegalArgumentException(s"Invalid joker color: $color")
            }
        } else {
            val number = try {
            value.toInt
            } catch {
            case _: NumberFormatException =>
                throw new IllegalArgumentException(s"Invalid token number: $value")
            }

            color match {
            case "red"   => tokenFactory.createNumToken(number, Color.RED)
            case "blue"  => tokenFactory.createNumToken(number, Color.BLUE)
            case "green" => tokenFactory.createNumToken(number, Color.GREEN)
            case "black" => tokenFactory.createNumToken(number, Color.BLACK)
            case _       => throw new IllegalArgumentException(s"Invalid token color: $color")
            }
        }
        }
    }

    def beginTurn(currentPlayer: PlayerInterface): Unit = {
        turnStartState = Some(getState)
        turnUndoManager = new UndoManager
    }

    def getState: GameState = playingField match {
        case Some(field) =>
        val copiedPlayers = field.getPlayers.map(_.deepCopy)
        val copiedBoards = field.getBoards.map(identity)
        GameState(
            table = field.innerField,
            players = copiedPlayers.toVector,
            boards = copiedBoards.toVector,
            currentPlayerIndex = currentPlayerIndex,
            stack = field.getStack
        )
        case None => GameState(
        table = null, 
        players = Vector.empty,
        boards = Vector.empty,
        currentPlayerIndex = 0,
        stack = null 
        )
    }

    def setStateInternal(state: GameState): Unit = {
        this.gameState = Some(state)
        this.playingField = Some(
        gameMode.playingFieldFactory.createPlayingField(state.players.toList, state.boards.toList, state.table, state.stack)
        )
        this.currentPlayerIndex = state.currentPlayerIndex
    }

    def executeAddRow(row: TokenStructureInterface, player: PlayerInterface, stack: TokenStackInterface): Unit = {
        val cmd = new AddRowCommand(this, row, player, stack)
        turnUndoManager.doStep(cmd)
    }

    def executeAddGroup(group: TokenStructureInterface, player: PlayerInterface, stack: TokenStackInterface): Unit = {
        val cmd = new AddGroupCommand(this, group, player, stack)
        turnUndoManager.doStep(cmd)
    }
    def executeAppendToRow(token: TokenInterface, rowIndex: Int, player: PlayerInterface): Unit = {
        val cmd = new AppendTokenCommand(this, token, rowIndex, isRow = true, player)
        turnUndoManager.doStep(cmd)
    }

    def executeAppendToGroup(token: TokenInterface, groupIndex: Int, player: PlayerInterface): Unit = {
        val cmd = new AppendTokenCommand(this, token, groupIndex, isRow = false, player)
        turnUndoManager.doStep(cmd)
    }

    def undo(): Unit = {
        turnUndoManager.undoStep()
    }
    def redo(): Unit = {
        turnUndoManager.redoStep()
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

    def playRow(
        tokenStrings: List[String],
        currentPlayer: PlayerInterface,
        stack: TokenStackInterface
    ): (PlayerInterface, String) = {
    val tokens = changeStringListToTokenList(tokenStrings, gameMode.tokenFactory)
    val row = gameMode.tokenStructureFactory.createRow(tokens)

    if (!row.isValid)
        return (currentPlayer, "Your move is not valid for the first move requirement.")

    val (_, updatedPlayer) = addRowToTable(row, currentPlayer)

    val updatedPlayerWithFlag = updatedPlayer.withCompletedFirstMove(true)

    val newState = getState.updateCurrentPlayer(updatedPlayerWithFlag)
    setStateInternal(newState)
    setPlayingField(gameMode.updatePlayingField(playingField))

    (updatedPlayerWithFlag, "Row successfully placed.")
    }


    def playGroup(tokenStrings: List[String], currentPlayer: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, String) = {
        val tokens = changeStringListToTokenList(tokenStrings, gameMode.tokenFactory)
        val group = gameMode.tokenStructureFactory.createGroup(tokens)
        if (!group.isValid)
        return (currentPlayer, "Your move is not valid for the first move requirement.")
        executeAddGroup(group, currentPlayer, stack)
        val updatedPlayer = getState.currentPlayer
        val updatedPlayerWithFlag = updatedPlayer.withCompletedFirstMove(true)
        val newState = getState.updateCurrentPlayer(updatedPlayerWithFlag)
        setStateInternal(newState)
        setPlayingField(gameMode.updatePlayingField(playingField))
        (updatedPlayerWithFlag, "Group successfully placed.")
    }

    def appendTokenToRow(tokenString: String, index: Int): (PlayerInterface, String) = {
        val tokenList = changeStringListToTokenList(List(tokenString), gameMode.tokenFactory)
        val token = tokenList.head
        val currentPlayer = getState.currentPlayer
        executeAppendToRow(token, index, currentPlayer)
        val updatedPlayer = getState.currentPlayer
        val newState = getState.updateCurrentPlayer(updatedPlayer)
        setStateInternal(newState)
        setPlayingField(gameMode.updatePlayingField(playingField))
        (updatedPlayer, s"Token appended to row at index $index.")
    }

    def appendTokenToGroup(tokenString: String, index: Int): (PlayerInterface, String) = {
        val tokenList = changeStringListToTokenList(List(tokenString), gameMode.tokenFactory)
        val token = tokenList.head
        val currentPlayer = getState.currentPlayer
        executeAppendToGroup(token, index, currentPlayer)
        val updatedPlayer = getState.currentPlayer
        val newState = getState.updateCurrentPlayer(updatedPlayer)
        setStateInternal(newState)
        setPlayingField(gameMode.updatePlayingField(playingField))
        (updatedPlayer, s"Token appended to group at index $index.")
    }

    def endGame(): Unit = {
        gameEnded = true
    }
}