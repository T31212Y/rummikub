package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.rummikub.util.TokenUtils.tokensMatch
import de.htwg.se.rummikub.util.{Command, UndoManager}

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color, TokenFactoryInterface}
import de.htwg.se.rummikub.model.playingFieldComponent.{TokenStackInterface, TokenStackFactoryInterface, PlayingFieldInterface, TableFactoryInterface}
import de.htwg.se.rummikub.model.gameModeComponent.{GameModeTemplate, GameModeFactoryInterface}
import de.htwg.se.rummikub.controller.controllerComponent.{ControllerInterface, UpdateEvent, GameStateInterface}
import de.htwg.se.rummikub.model.tokenStructureComponent.{TokenStructureInterface, TokenStructureFactoryInterface}
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableInterface

import scala.swing.Publisher

import com.google.inject.Inject

class Controller @Inject() (gameModeFactory: GameModeFactoryInterface, 
                            tokenFactory: TokenFactoryInterface, 
                            tokenStructureFactory: TokenStructureFactoryInterface,
                            tokenStackFactory: TokenStackFactoryInterface,
                            tableFactory: TableFactoryInterface,
                            playingFieldBuilder: PlayingFieldBuilderInterface) extends ControllerInterface with Publisher {

    var gameMode: Option[GameModeTemplate] = None
    var playingField: Option[PlayingFieldInterface] = None
    var gameState: Option[GameStateInterface] = None
    var currentPlayerIndex: Int = 0
    var turnStartState: Option[GameStateInterface] = None

    var turnUndoManager: UndoManager = new UndoManager
    var gameEnded: Boolean = false

    override def setupNewGame(amountPlayers: Int, names: List[String]): Unit = {
        gameMode = gameModeFactory.createGameMode(amountPlayers, names).toOption
        playingField = gameMode.flatMap(_.runGameSetup)

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

        val newState = gameState.get.updated(newPlayers = updatedPlayers, newStack = updatedStack, newFinalRoundsLeft = None)
        setStateInternal(newState)
        publish(UpdateEvent())
    }

    override def setPlayingField(pf: Option[PlayingFieldInterface]): Unit = {
        this.playingField = pf
        publish(UpdateEvent())
    }

    override def playingFieldToString: String = {
        gameMode.get.renderPlayingField(playingField)
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

        if (!ignoreFirstMoveCheck && !currentPlayer.getHasCompletedFirstMove && !currentPlayer.validateFirstMove) {
            val message = "The first move must have a total of at least 30 points. You cannot end your turn."
            return (state, message)
        }

        val tableValid = gameMode.get.isValidTable(state.getTable.getTokensOnTable)
        if (!tableValid) {
            val message = "You cannot end your turn. The table is not valid!"
            return (state, message)
        }

        val updatedPlayer = if (!currentPlayer.getHasCompletedFirstMove) {
            currentPlayer.updated(
            currentPlayer.getTokens,
            newCommandHistory = currentPlayer.getCommandHistory,
            newHasCompletedFirstMove = true
            )
        } else currentPlayer

        val nextState = setNextPlayer(state.updateCurrentPlayer(updatedPlayer))
        turnStartState = None
        val message = s"${currentPlayer.getName} ended their turn. It's now ${nextState.currentPlayer.getName}'s turn."

        setStateInternal(nextState)
        setPlayingField(gameMode.get.updatePlayingField(playingField))
        publish(UpdateEvent())

        (nextState, message)
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

    override def addRowToTable(row: TokenStructureInterface, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface) = {
        val unmatched = row.getTokens.filterNot(tokenInRow =>
          currentPlayer.getTokens.exists(playerToken => tokensMatch(tokenInRow, playerToken))
        )
      
        if (unmatched.nonEmpty) {
            println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
            (List.empty, currentPlayer)
        } else {
            val remainingTokens = currentPlayer.getTokens.filterNot(playerToken =>
                row.getTokens.exists(tokenInRow => tokensMatch(tokenInRow, playerToken))
            )

            val updatedPlayer = currentPlayer.updated(newTokens = remainingTokens, newCommandHistory = currentPlayer.getCommandHistory :+ s"row:${row.getTokens.mkString(",")}", newHasCompletedFirstMove = currentPlayer.getHasCompletedFirstMove).addToFirstMoveTokens(row.getTokens)
            val updatedTable = playingField.get.getInnerField.add(row.getTokens)

            val updatedPlayers = playingField.get.getPlayers.map {
                    case p if p.getName == currentPlayer.getName => updatedPlayer
                    case p => p
            }

            val newState = getState.updateCurrentPlayer(updatedPlayer).updateTable(updatedTable).updatePlayers(updatedPlayers.toVector)

            setStateInternal(newState)

            (row.getTokens, updatedPlayer)
        }
    }

    override def addGroupToTable(group: TokenStructureInterface, currentPlayer: PlayerInterface): (List[TokenInterface], PlayerInterface) = {
        val unmatched = group.getTokens.filterNot(tokenInGroup =>
          currentPlayer.getTokens.exists(playerToken => tokensMatch(tokenInGroup, playerToken))
        )

        if (unmatched.nonEmpty) {
            println(s"You can only play tokens that are on your board: ${unmatched.mkString(", ")}")
            (List.empty, currentPlayer)
        } else {
            val remainingTokens = currentPlayer.getTokens.filterNot(playerToken =>
                group.getTokens.exists(tokenInGroup => tokensMatch(tokenInGroup, playerToken))
            )

            val updatedPlayer = currentPlayer.updated(newTokens = remainingTokens, newCommandHistory = currentPlayer.getCommandHistory :+ s"group:${group.getTokens.mkString(",")}", newHasCompletedFirstMove = currentPlayer.getHasCompletedFirstMove).addToFirstMoveTokens(group.getTokens)
            val updatedTable = playingField.get.getInnerField.add(group.getTokens)

            val updatedPlayers = playingField.get.getPlayers.map {
                    case p if p.getName == currentPlayer.getName => updatedPlayer
                    case p => p
            }

            val newState = getState.updateCurrentPlayer(updatedPlayer).updateTable(updatedTable).updatePlayers(updatedPlayers.toVector)

            setStateInternal(newState)

            (group.getTokens, updatedPlayer)
        }
    }

    override def changeStringListToTokenList(list: List[String]): List[TokenInterface] = { 
        list.map { tokenString =>
            val tokenParts = tokenString.split(":")
            if (tokenParts.length < 2)
                throw new IllegalArgumentException("Invalid token input.")

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
            val finalRounds = gameState.map(_.getFinalRoundsLeft).getOrElse(None)
            val storageTokens = gameState.map(_.getStorageTokens).getOrElse(Vector.empty)

            GameState(
            table = field.getInnerField,
            players = copiedPlayers.toVector,
            boards = copiedBoards.toVector,
            currentPlayerIndex = currentPlayerIndex,
            stack = field.getStack,
            finalRoundsLeft = finalRounds,
            storageTokens = storageTokens
            )

        case None => GameState(
            table = tableFactory.createTable(16, 90, List.empty),
            players = Vector.empty,
            boards = Vector.empty,
            currentPlayerIndex = 0,
            stack = tokenStackFactory.createShuffledStack,
            finalRoundsLeft = None,
            storageTokens = Vector.empty
        )
    }


    override def setStateInternal(state: GameStateInterface): Unit = {
        this.gameState = Some(state)

        val buildedPlayingField = playingFieldBuilder
            .setPlayers(state.getPlayers.toList)
            .setBoards(state.getBoards.toList)
            .setInnerField(state.getTable)
            .setStack(state.currentStack)
            .build()

        this.playingField = Some(buildedPlayingField)
        this.currentPlayerIndex = state.getCurrentPlayerIndex
    }

    override def executeAddRow(row: TokenStructureInterface, player: PlayerInterface, stack: TokenStackInterface): Unit = {
        val cmd = new AddRowCommand(this, row, player, stack)
        turnUndoManager.doStep(cmd)
    }

    override def executeAddGroup(group: TokenStructureInterface, player: PlayerInterface, stack: TokenStackInterface): Unit = {
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
        setPlayingField(gameMode.get.updatePlayingField(playingField))
        (finalState, message)
    }

    override def playRow(tokenStrings: List[String], unusedPlayer: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, String) = {
        val currentPlayer = getState.currentPlayer
        val tokens = changeStringListToTokenList(tokenStrings)
        val row = tokenStructureFactory.createRow(tokens)

        if (!row.isValid)
            return (currentPlayer, "Your move is not valid for the first move requirement.")

        if (!currentPlayer.getHasCompletedFirstMove) {
            val tentativePlayer = currentPlayer.addToFirstMoveTokens(row.getTokens)
            if (!tentativePlayer.validateFirstMove) {
                return (currentPlayer, "First move must total at least 30 points with valid sets.")
            }
        }

        executeAddRow(row, currentPlayer, stack)

        val updatedPlayer = currentPlayer
            .addToFirstMoveTokens(row.getTokens)
            .updated(
                newTokens = getUpdatedPlayerAfterMove(getState.currentPlayer, row.getTokens).getTokens,
                newCommandHistory = currentPlayer.getCommandHistory :+ s"playRow: ${row.getTokens.mkString(",")}",
                newHasCompletedFirstMove = true
            )

        val newState = getState.updateCurrentPlayer(updatedPlayer)

        setStateInternal(newState)
        setPlayingField(gameMode.get.updatePlayingField(playingField))

        (updatedPlayer, "Row successfully placed.")
    }


    override def playGroup(tokenStrings: List[String], unusedPlayer: PlayerInterface, stack: TokenStackInterface): (PlayerInterface, String) = {
        val currentPlayer = getState.currentPlayer
        val tokens = changeStringListToTokenList(tokenStrings)
        val group = tokenStructureFactory.createGroup(tokens)

        if (!group.isValid)
            return (currentPlayer, "Your move is not valid for the first move requirement.")

        if (!currentPlayer.getHasCompletedFirstMove) {
            val tentativePlayer = currentPlayer.addToFirstMoveTokens(group.getTokens)
            if (!tentativePlayer.validateFirstMove) {
                return (currentPlayer, "First move must total at least 30 points with valid sets.")
            }
        }

        executeAddGroup(group, currentPlayer, stack)

        val updatedPlayer = currentPlayer
            .addToFirstMoveTokens(group.getTokens)
            .updated(
                newTokens = getUpdatedPlayerAfterMove(getState.currentPlayer, group.getTokens).getTokens,
                newCommandHistory = currentPlayer.getCommandHistory :+ s"playGroup: ${group.getTokens.mkString(",")}",
                newHasCompletedFirstMove = true // immer true nach erstem gültigen Zug!
            )

        val newState = getState.updateCurrentPlayer(updatedPlayer)

        setStateInternal(newState)
        setPlayingField(gameMode.get.updatePlayingField(playingField))

        (updatedPlayer, "Group successfully placed.")
    }

    override def appendTokenToRow(tokenString: String, index: Int): (PlayerInterface, String) = {
        val tokenList = changeStringListToTokenList(List(tokenString))

        val token = tokenList.head
        val currentPlayer = getState.currentPlayer

        executeAppendToRow(token, index, currentPlayer)

        val updatedPlayer = currentPlayer
        .updated(
            newTokens = getUpdatedPlayerAfterMove(getState.currentPlayer,  List(token)).getTokens,
            newCommandHistory = currentPlayer.getCommandHistory :+ s"appendToRow: ${List(token).mkString(",")}",
            newHasCompletedFirstMove = currentPlayer.getHasCompletedFirstMove
        )

        val newState = getState.updateCurrentPlayer(updatedPlayer)

        setStateInternal(newState)
        setPlayingField(gameMode.get.updatePlayingField(playingField))

        (updatedPlayer, s"Token appended to row at index $index.")
    }

    override def appendTokenToGroup(tokenString: String, index: Int): (PlayerInterface, String) = {
        val tokenList = changeStringListToTokenList(List(tokenString))

        val token = tokenList.head
        val currentPlayer = getState.currentPlayer

        executeAppendToGroup(token, index, currentPlayer)

        val updatedPlayer = currentPlayer
        .updated(
            newTokens = getUpdatedPlayerAfterMove(getState.currentPlayer,  List(token)).getTokens,
            newCommandHistory = currentPlayer.getCommandHistory :+ s"appendToGroup: ${List(token).mkString(",")}",
            newHasCompletedFirstMove = currentPlayer.getHasCompletedFirstMove
        )

        val newState = getState.updateCurrentPlayer(updatedPlayer)

        setStateInternal(newState)
        setPlayingField(gameMode.get.updatePlayingField(playingField))

        (updatedPlayer, s"Token appended to group at index $index.")
    }
    
    override def endGame: String = {
        val players = getState.getPlayers

        val winner = players.minBy(_.getTokens.size)
        val minCount = winner.getTokens.size
        val winners = players.filter(_.getTokens.size == minCount)

        val winnerMessage = if (winners.size == 1) {
            s"The winner is: ${winner.getName} with $minCount tokens left! Congratulations!"
        } else {
            s"It's a tie between: ${winners.map(_.getName).mkString(", ")} with $minCount tokens each! Well done!"
        }
        winnerMessage
    }

    override def getGameEnded: Boolean = gameEnded

    override def getGameMode: GameModeTemplate = gameMode.get

    override def getPlayingField: Option[PlayingFieldInterface] = playingField

    override def setGameEnded(nge: Boolean): Unit = {
        gameEnded = nge
    }

    override def getTurnStartState: Option[GameStateInterface] = turnStartState

    override def setTurnStartState(newState: Option[GameStateInterface]): Unit = {
        turnStartState = newState
    }

    override def getUndoManager: UndoManager = turnUndoManager

    override def setUndoManager(num: UndoManager): Unit = {
        turnUndoManager = num
    }

    override def getTokenFromString(tokenStr: String): TokenInterface = {
        val Array(numStr, colorStr) = tokenStr.split(":")
        val color = Color.values.find(_.name == colorStr.trim.toLowerCase)
            .getOrElse(throw new IllegalArgumentException(s"Unknown color: $colorStr"))

        if (numStr.trim == "J")
            tokenFactory.createJoker(color)
        else
            tokenFactory.createNumToken(numStr.trim.toInt, color)
    }


    def getIndexedTokensOnTable: List[(Int, TokenInterface)] = {
    val table = getState.getTable
    val tokens = table.getTokensOnTable.flatten
    tokens.zipWithIndex.map { case (token, idx) => (idx, token) }
    }

    def getColoredTokenString(token: TokenInterface): String = {
        val numberStr = token.getNumber.map(_.toString).getOrElse("J")
        val colorName = token.getColor.name.capitalize
        s"$numberStr ($colorName)"
    }
    def getDisplayStringForTokensWithIndex: String = {
        getIndexedTokensOnTable.map { case (idx, token) =>
            s"[$idx] ${getColoredTokenString(token)}"
        }.mkString("\n ")
    }

    override def putTokenInStorage(tokenId: Int): Option[GameStateInterface] = {
        val table = getState.getTable
        val tokensOnTable = table.getTokensOnTable

        var currentIndex = 0
        val updatedTokensOnTable = tokensOnTable.map { rowOrGroup =>
            rowOrGroup.flatMap { token =>
            if (currentIndex == tokenId) {
                currentIndex += 1
                None 
            } else {
                currentIndex += 1
                Some(token)
            }
            }
        }.filter(_.nonEmpty)

        if (tokenId < 0 || tokenId >= currentIndex) {
            None
        } else {
            val token = tokensOnTable.flatten.apply(tokenId)
            val updatedTable = table.updated(updatedTokensOnTable)
            val tokenStr = s"${token.getNumber.map(_.toString).getOrElse("J")}:${token.getColor.name.toLowerCase}"
            val updatedStorage = getState.getStorageTokens :+ tokenStr 
            val newState = getState.updatedStorage(updatedStorage).updateTable(updatedTable)
            Some(newState)
        }
    }


    def getFormattedTokensOnTableWithLabels: String = {
    val table = getState.getTable
    val rowsCount = table.getCntRows
    val tokensOnTable = table.getTokensOnTable

    var globalIndex = 0

    val rowStrings = tokensOnTable.take(rowsCount).zipWithIndex.map { case (tokens, idx) =>
        val tokensStr = tokens.map { token =>
        val s = s"[$globalIndex] ${token.getNumber.map(_.toString).getOrElse("J")} (${token.getColor.name})"
        globalIndex += 1
        s
        }.mkString(", ")
        s"row$idx: $tokensStr"
    }

    val groupStrings = tokensOnTable.drop(rowsCount).zipWithIndex.map { case (tokens, idx) =>
        val tokensStr = tokens.map { token =>
        val s = s"[$globalIndex] ${token.getNumber.getOrElse("")} (${token.getColor.name})"
        globalIndex += 1
        s
        }.mkString(", ")
        s"group$idx: $tokensStr"
    }

    (rowStrings ++ groupStrings).mkString("\n")
    }

    def fromStorageToTable(table: TableInterface, tokenStr: String, groupIndex: Int, insertAtIndex: Int): TableInterface = {
        val token = getTokenFromString(tokenStr)
        val tableGroups = table.getTokensOnTable

        if (groupIndex < 0 || groupIndex >= tableGroups.length)
            throw new IndexOutOfBoundsException(s"Group index $groupIndex is invalid.")

        val group = tableGroups(groupIndex)

        if (insertAtIndex < 0 || insertAtIndex > group.length)
            throw new IndexOutOfBoundsException(s"Insert index $insertAtIndex is invalid in group $groupIndex.")

        val updatedGroup = group.patch(insertAtIndex, Seq(token), 0)
        val updatedGroups = tableGroups.updated(groupIndex, updatedGroup)

        table.updated(updatedGroups)
    }

    override def fromStorageToTable(state: GameStateInterface, tokenStr: String, groupIndex: Int, insertAtIndex: Int): (GameStateInterface, String) = {
    val storage = state.getStorageTokens
    if (!storage.contains(tokenStr)) {
        return (state, "Token nicht im Storage gefunden!")
    }

    val token = getTokenFromString(tokenStr)
    val table = state.getTable
    val tokensOnTable = table.getTokensOnTable

    if (groupIndex < 0 || groupIndex >= tokensOnTable.length) {
        return (state, "Ungültiger Gruppen-Index!")
    }

    val group = tokensOnTable(groupIndex)
    if (insertAtIndex < 0 || insertAtIndex > group.length) {
        return (state, "Ungültige Einfügeposition!")
    }

    val updatedGroup = group.patch(insertAtIndex, Seq(token), 0)
    val updatedTokensOnTable = tokensOnTable.updated(groupIndex, updatedGroup)
    val updatedTable = table.updated(updatedTokensOnTable)

    val updatedStorage = storage.filterNot(_ == tokenStr)
    val newState = state
        .updatedStorage(updatedStorage)
        .updateTable(updatedTable)

    (newState, s"Token $tokenStr wurde an Position $insertAtIndex in Gruppe $groupIndex eingefügt.")
    }
}

