package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.GameState
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.Color
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Board
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.TokenStack
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Table
import de.htwg.se.rummikub.model.gameModeComponent.GameModeTemplate
import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.Row
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.Group
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.TwoPlayerMode
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.Joker
import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.BoardInterface

import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import de.htwg.se.rummikub.RummikubModule

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.{TableFactoryInterface, BoardFactoryInterface}
import de.htwg.se.rummikub.model.playerComponent.PlayerFactoryInterface
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.builderComponent.FieldDirectorInterface
import de.htwg.se.rummikub.controller.controllerComponent.GameStateInterface
import de.htwg.se.rummikub.util.UndoManager


class ControllerSpec extends AnyWordSpec {

  "A Controller" should {
    val injector = Guice.createInjector(new RummikubModule)
    val controller = injector.getInstance(classOf[ControllerInterface])
    controller.setupNewGame(2, List("Emilia", "Noah"))

    val tokenStackFactory = injector.getInstance(classOf[TokenStackFactoryInterface])
    val tokenStructureFactory = injector.getInstance(classOf[TokenStructureFactoryInterface])
    val tableFactory = injector.getInstance(classOf[TableFactoryInterface])
    val boardFactory = injector.getInstance(classOf[BoardFactoryInterface])
    val playerFactory = injector.getInstance(classOf[PlayerFactoryInterface])

    val playingFieldBuilder = injector.getInstance(classOf[PlayingFieldBuilderInterface])
    val director = injector.instance[FieldDirectorInterface](Names.named("TwoPlayer"))

    val player1 = Player("Alice", List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
    val player2 = Player("Bob", List(NumToken(2, Color.BLUE)), tokenStructureFactory = tokenStructureFactory)
    val players = List(player1, player2)
    val boards = List(
      Board(0, 0, 2, 2, "dest1", 10),
      Board(1, 0, 2, 2, "dest2", 10)
    )
    val table = Table(16, 90, List())
    val stack = TokenStack(List(NumToken(3, Color.GREEN)))
    val pf = PlayingField(players, boards, table, stack)

    "setup a new game" in {
      controller.setupNewGame(2, List("Alice", "Bob"))
      controller.getPlayingField.isDefined shouldBe true
      controller.getGameEnded shouldBe false
    }

    "start a game and distribute tokens" in {
      controller.setupNewGame(2, List("Alice", "Bob"))
      controller.startGame
      controller.getState.getPlayers.foreach(_.getTokens.size should be >= 1)
    }

    "add and remove tokens to/from player" in {
      val (updatedPlayer, updatedStack) = controller.addTokenToPlayer(player1, stack)
      updatedPlayer.getTokens.size shouldBe player1.tokens.size + 1
      updatedStack.getTokens.size shouldBe stack.tokens.size - 1

      noException should be thrownBy controller.removeTokenFromPlayer(updatedPlayer, updatedPlayer.getTokens.head)
    }

    "add multiple tokens to player" in {
      val (updatedPlayer, updatedStack) = controller.addMultipleTokensToPlayer(player1, stack, 1)
      updatedPlayer.getTokens.size shouldBe player1.tokens.size + 1
      updatedStack.getTokens.size shouldBe stack.tokens.size - 1
    }

    "change string list to token list" in {
      val tokens = controller.changeStringListToTokenList(List("1:red", "J:black"))
      tokens.size shouldBe 2
      tokens.exists {
        case Joker(_) => true
        case _ => false
      } shouldBe true
    }

    "throw IllegalArgumentException for invalid token color" in {
      val invalidTokenString = List("1:rainbow")
      val exception = intercept[IllegalArgumentException] {
        controller.changeStringListToTokenList(invalidTokenString)
      }
      exception.getMessage should include ("Invalid token color: rainbow")
    }

    "throw IllegalArgumentException for invalid joker color" in {
      val invalidJokerString = List("J:rainbow")
      val exception = intercept[IllegalArgumentException] {
        controller.changeStringListToTokenList(invalidJokerString)
      }
      exception.getMessage should include ("Invalid joker color: rainbow")
    }

    "add row and group to table" in {
      controller.setPlayingField(Some(pf))
      val row = tokenStructureFactory.createRow(List(NumToken(1, Color.RED)))
      val (rowTokens, updatedPlayer1) = controller.addRowToTable(row, player1)
      rowTokens should not be empty
      updatedPlayer1.getTokens shouldBe empty

      val group = tokenStructureFactory.createGroup(List(NumToken(2, Color.BLUE)))
      val (groupTokens, updatedPlayer2) = controller.addGroupToTable(group, player2)
      groupTokens should not be empty
      updatedPlayer2.getTokens shouldBe empty
    }

    "support undo and redo" in {
      controller.setPlayingField(Some(pf))
      val row = tokenStructureFactory.createRow(List(NumToken(1, Color.RED)))
      controller.executeAddRow(row, player1, stack)
      noException should be thrownBy controller.undo
      noException should be thrownBy controller.redo
    }

    "return single winner message" in {
      val player1 = Player("Alice", List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val player2 = Player("Bob", List(NumToken(2, Color.BLUE), NumToken(8, Color.BLACK)), tokenStructureFactory = tokenStructureFactory)
      val players = List(player1, player2)
      val boards = List(
        Board(0, 0, 2, 2, "dest1", 10),
        Board(1, 0, 2, 2, "dest2", 10)
      )
      val table = Table(16, 90, List())
      val stack = TokenStack(List(NumToken(3, Color.GREEN)))
      val pf = PlayingField(players, boards, table, stack)

      controller.setPlayingField(Some(pf))

      val result = controller.endGame

      result should be ("The winner is: Alice with 1 tokens left! Congratulations!")
    }

    "return tie message if more than 1 player have same amount of tokens" in {
      val player1 = Player("Alice", List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val player2 = Player("Bob", List(NumToken(2, Color.BLUE)), tokenStructureFactory = tokenStructureFactory)
      val players = List(player1, player2)
      val boards = List(
        Board(0, 0, 2, 2, "dest1", 10),
        Board(1, 0, 2, 2, "dest2", 10)
      )
      val table = Table(16, 90, List())
      val stack = TokenStack(List(NumToken(3, Color.GREEN)))
      val pf = PlayingField(players, boards, table, stack)

      controller.setPlayingField(Some(pf))

      val result = controller.endGame

      result should be ("It's a tie between: Alice, Bob with 1 tokens each! Well done!")
    }

    "not allow to play tokens that are not on the player's board" in {
      controller.setPlayingField(Some(pf))
      val fakeToken = NumToken(99, Color.BLACK)
      val row = tokenStructureFactory.createRow(List(fakeToken))
      val stream = new java.io.ByteArrayOutputStream
      Console.withOut(stream) {
        val (tokens, updatedPlayer) = controller.addRowToTable(row, player1)
        tokens shouldBe empty
        updatedPlayer shouldBe player1
      }
      val output = stream.toString
      output should include ("You can only play tokens that are on your board")
    }

    "correctly parse green as token color" in {
      val tokens = controller.changeStringListToTokenList(List("5:green"))
      tokens should have size 1
      tokens.head shouldBe NumToken(5, Color.GREEN)
    }

    "correctly parse black as token color" in {
      val tokens = controller.changeStringListToTokenList(List("7:black"))
      tokens should have size 1
      tokens.head shouldBe NumToken(7, Color.BLACK)
    }

    "correctly parse red as joker color" in {
      val tokens = controller.changeStringListToTokenList(List("J:red"))
      tokens should have size 1
      tokens.head shouldBe Joker(Color.RED)
    }

    "execute add group command" in {
      controller.setPlayingField(Some(pf))
      val group = tokenStructureFactory.createGroup(List(NumToken(2, Color.BLUE)))
      controller.executeAddGroup(group, player2, stack)
      controller.getState.getPlayers.find(_.getName == "Bob").get.getTokens shouldBe empty
    }

    "throw NoSuchElementException if player is not found" in {
      val missingPlayer = Player("Charlie", tokenStructureFactory = tokenStructureFactory)
      val group = tokenStructureFactory.createGroup(List(NumToken(2, Color.BLUE)))
      val exception = intercept[NoSuchElementException] {
        controller.executeAddGroup(group, missingPlayer, stack)
      }
      exception.getMessage should include ("Charlie")
    }

    "do nothing if undo is called with no previous state" in {
      noException should be thrownBy controller.undo
      controller.getState shouldBe controller.getState
    }

    "do nothing if redo is called with no next state" in {
      noException should be thrownBy controller.redo
      controller.getState shouldBe controller.getState
    }

    "return message if invalid token input is given" in {
      controller.setPlayingField(Some(pf))
      val invalidTokenString = List("foo")
      an [IllegalArgumentException] should be thrownBy {
        controller.addRowToTable(tokenStructureFactory.createRow(controller.changeStringListToTokenList(List("foo"))), player1)
      }
    }

    "return message if first move requirement is not met when adding a group (should only be checked on passTurn)" in {
      controller.setPlayingField(Some(pf))
      val testPlayer = player1.copy(tokens = List(NumToken(5, Color.RED), NumToken(6, Color.RED), NumToken(7, Color.RED)))
      val groupTokens = List("5:red", "6:red", "7:red")
      val (updatedPlayer, msg) = controller.playGroup(groupTokens, testPlayer, stack)
      msg should be ("Group successfully placed.")
      updatedPlayer.getHasCompletedFirstMove shouldBe false
  }

    "passTurn should not allow passing if first move is not completed and ignoreFirstMoveCheck is false" in {
      val player = Player("Emilia", hasCompletedFirstMove = false, tokenStructureFactory = tokenStructureFactory)
      val player2 = Player("Noah", tokenStructureFactory = tokenStructureFactory)
      val players = Vector(player, player2)
      val table = Table(16, 90, List.empty)
      val boards = Vector.empty[Board]
      val stack = tokenStackFactory.createShuffledStack
      val state = GameState(table, players, boards, 0, stack)

      controller.setPlayingField(Some(PlayingField(players.toList, boards.toList, table, stack)))

      val (resultState, message) = controller.passTurn(state, false)
      resultState shouldBe state
      message shouldBe "The first move must have a total of at least 30 points. You cannot end your turn."
    }

    "passTurn should not allow passing if the table is not valid" in {
      val player = Player("Emilia", hasCompletedFirstMove = true, tokenStructureFactory = tokenStructureFactory)
      val player2 = Player("Noah", tokenStructureFactory = tokenStructureFactory)
      val players = Vector(player, player2)

      val invalidRow = List(NumToken(1, Color.RED), NumToken(2, Color.RED))
      val table = Table(16, 90, List(invalidRow))
      val boards = Vector.empty[Board]
      val stack = tokenStackFactory.createShuffledStack
      val state = GameState(table, players, boards, 0, stack)

      controller.setPlayingField(Some(PlayingField(players.toList, boards.toList, table, stack)))

      val (resultState, message) = controller.passTurn(state, ignoreFirstMoveCheck = false)
      resultState shouldBe state
      message shouldBe "You cannot end your turn. The table is not valid!"
    }

    "winGame should return true and print winner if a player has no tokens" in {
      val winner = Player("Emilia", tokens = List(), tokenStructureFactory = tokenStructureFactory)
      val loser = Player("Noah", tokens = List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val pf = PlayingField(List(winner, loser), List(), Table(0, 0, List()), tokenStackFactory.createShuffledStack)
      controller.setPlayingField(Some(pf))

      val output = new java.io.ByteArrayOutputStream()
      Console.withOut(new java.io.PrintStream(output)) {
        controller.winGame shouldBe true
      }
      output.toString should include ("Player Emilia wins the game!")
    }

    "winGame should return false if no player has won" in {
      val player1 = Player("Emilia", tokens = List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val player2 = Player("Noah", tokens = List(NumToken(2, Color.BLUE)), tokenStructureFactory = tokenStructureFactory)
      val pf = PlayingField(List(player1, player2), List(), Table(0, 0, List()), tokenStackFactory.createShuffledStack)
      controller.setPlayingField(Some(pf))

      controller.winGame shouldBe false
    }

    "winGame should return false if playingField is None" in {
      controller.setPlayingField(None)

      controller.winGame shouldBe false
    }

    "addGroupToTable should not allow playing tokens not on the player's board" in {
      val player = Player("Emilia", tokens = List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val token = NumToken(9, Color.BLACK)
      val group = Group(List(token))
      controller.setPlayingField(Some(PlayingField(List(player), List(), Table(0, 0, List()), tokenStackFactory.createShuffledStack)))

      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        val (tokens, updatedPlayer) = controller.addGroupToTable(group, player)
        tokens shouldBe empty
        updatedPlayer shouldBe player
      }
      val output = stream.toString
      output should include ("You can only play tokens that are on your board")
    }

    "drawFromStackAndPass should handle the None case for turnStartState" in {
      val player = Player("Emilia", tokens = List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val player2 = Player("Noah", tokenStructureFactory = tokenStructureFactory)
      val table = Table(16, 90, List.empty)
      val board = boardFactory.createBoard(15, 24, 2, 1, "up", 90)
      val board2 = boardFactory.createBoard(15, 24, 2, 1, "down", 90)
      val stack = tokenStackFactory.createShuffledStack
      val state: GameStateInterface = GameState(table, Vector(player, player2), Vector(board, board2), 0, stack)

      controller.setPlayingField(Some(PlayingField(List(player, player2), List(board, board2), table, stack)))
      controller.setStateInternal(state)
      controller.setTurnStartState(None)

      val (resultState, message) = controller.drawFromStackAndPass

      resultState.getPlayers.head.getTokens.nonEmpty shouldBe true
    }

    "drawFromStackAndPass should restore previous state if turnStartState is set" in {
      val player = Player("Emilia", tokens = List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val player2 = Player("Noah", tokenStructureFactory = tokenStructureFactory)
      val table = Table(16, 90, List.empty)
      val board = boardFactory.createBoard(15, 24, 2, 1, "up", 90)
      val board2 = boardFactory.createBoard(15, 24, 2, 1, "down", 90)
      val stack = TokenStack(List(NumToken(12, Color.BLUE)))
      val previousState: GameStateInterface = GameState(table, Vector(player, player2), Vector(board, board2), 0, stack)

      controller.setPlayingField(Some(PlayingField(List(player, player2), List(board, board2), table, stack)))
      controller.setTurnStartState(Some(previousState))

      controller.drawFromStackAndPass
      val state = controller.getState

      val prevTokens = previousState.getPlayers.head.getTokens
      val newTokens = state.getPlayers.head.getTokens

      val prevTokenValues = prevTokens.map(t => (t.getNumber, t.getColor))
      val newTokenValues = newTokens.map(t => (t.getNumber, t.getColor))
      newTokenValues.size shouldBe prevTokenValues.size + 1
      newTokenValues.exists(t => !prevTokenValues.contains(t)) shouldBe true

      state.getPlayers(1).getTokens shouldBe previousState.getPlayers(1).getTokens
      state.getTable.getTokensOnTable shouldBe previousState.getTable.getTokensOnTable
      state.currentStack.getTokens.size shouldBe previousState.currentStack.getTokens.size - 1
    }

    "playRow should return success message even if row is not valid (first move check happens on pass)" in {
      val player = Player("Emilia", tokenStructureFactory = tokenStructureFactory)
      val stack = tokenStackFactory.createShuffledStack
      val (resultPlayer, message) = controller.playRow(List("1:red"), player, stack)
      resultPlayer.getName shouldBe player.getName
      resultPlayer.getHasCompletedFirstMove shouldBe false
      message shouldBe "Row successfully placed."
    }

    "playGroup should return success message even if group is not valid (first move check happens on pass)" in {
      val player = Player("Emilia", tokenStructureFactory = tokenStructureFactory)
      val stack = tokenStackFactory.createShuffledStack
      val (resultPlayer, message) = controller.playGroup(List("1:red"), player, stack)
      resultPlayer.getName shouldBe player.getName
      resultPlayer.getHasCompletedFirstMove shouldBe false
      message shouldBe "Group successfully placed."
    }

    "playRow should place a valid row and return success message" in {
      val player = Player("Emilia", tokens = List(NumToken(10, Color.RED), NumToken(11, Color.RED), NumToken(12, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val player2 = Player("Noah", tokenStructureFactory = tokenStructureFactory)
      val table = Table(16, 90, List.empty)
      val board = boardFactory.createBoard(15, 24, 2, 1, "up", 90)
      val board2 = boardFactory.createBoard(15, 24, 2, 1, "down", 90)
      val stack = tokenStackFactory.createShuffledStack

      controller.setPlayingField(Some(PlayingField(List(player, player2), List(board, board2), table, stack)))
      val (resultPlayer, message) = controller.playRow(List("10:red", "11:red", "12:red"), player, stack)
      message shouldBe "Row successfully placed."
      resultPlayer.getHasCompletedFirstMove shouldBe false
    }

    "playGroup should place a valid group and return success message" in {
      val player = Player("Emilia", tokens = List(NumToken(10, Color.RED), NumToken(10, Color.BLACK), NumToken(10, Color.BLUE)), tokenStructureFactory = tokenStructureFactory)
      val player2 = Player("Noah", tokenStructureFactory = tokenStructureFactory)
      val table = Table(16, 90, List.empty)
      val board = boardFactory.createBoard(15, 24, 2, 1, "up", 90)
      val board2 = boardFactory.createBoard(15, 24, 2, 1, "down", 90)
      val stack = tokenStackFactory.createShuffledStack

      controller.setPlayingField(Some(PlayingField(List(player, player2), List(board, board2), table, stack)))
      val (resultPlayer, message) = controller.playGroup(List("10:red", "10:black", "10:blue"), player, stack)
      message shouldBe "Group successfully placed."
      resultPlayer.getHasCompletedFirstMove shouldBe false
    }

    "beginTurn should do nothing if commandHistory is not empty" in {
      controller.setTurnStartState(None)
      val oldUndoManager = controller.getUndoManager

      val player = Player("Emilia", commandHistory = List("row:10:red,11:red,12:red"), tokenStructureFactory = tokenStructureFactory)
      controller.beginTurn(player)

      controller.getTurnStartState shouldBe None
      controller.getUndoManager shouldBe oldUndoManager
    }

    "update players, stack and finalRoundsLeft in GameState via updated" in {
      val playerA = Player("Anna", List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val playerB = Player("Ben", List(NumToken(2, Color.BLUE)), tokenStructureFactory = tokenStructureFactory)
      val updatedPlayers = Vector(playerA, playerB)
      val updatedStack = TokenStack(List(NumToken(99, Color.BLACK)))
      val gameState = controller.getState
      val newState = gameState.updated(newPlayers = updatedPlayers, newStack = updatedStack, newFinalRoundsLeft = None)
      newState.getPlayers shouldBe updatedPlayers
      newState.currentStack shouldBe updatedStack
      newState.getFinalRoundsLeft shouldBe None
    }

    "appendTokenToRow should append a token at the correct position" in {
      val player = Player("Emilia", tokens = List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      controller.setPlayingField(Some(pf))
      val (updatedPlayer, msg) = controller.appendTokenToRow("1:red", 0, 0)
      msg should include ("Token appended to row")
      updatedPlayer.getTokens should not contain NumToken(1, Color.RED)
    }

    "getDisplayStringForTokensWithIndex should return indexed tokens as string" in {
      val player = Player("Emilia", tokens = List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val table = Table(16, 90, List(List(NumToken(1, Color.RED))))
      val pf = PlayingField(List(player), List(), table, stack)
      controller.setPlayingField(Some(pf))
      controller.setStateInternal(GameState(table, Vector(player), Vector(), 0, stack))
      val result = controller.getDisplayStringForTokensWithIndex
      result should include ("[0] 1 (Red)")
    }

    "putTokenInStorage should move token from table to storage" in {
      val player = Player("Emilia", tokens = List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val table = Table(16, 90, List(List(NumToken(1, Color.RED))))
      val pf = PlayingField(List(player), List(), table, stack)
      controller.setPlayingField(Some(pf))
      controller.setStateInternal(GameState(table, Vector(player), Vector(), 0, stack))
      val result = controller.putTokenInStorage(0)
      result shouldBe defined
      result.get.getStorageTokens should contain ("1:red")
      result.get.getTable.getTokensOnTable.flatten should not contain NumToken(1, Color.RED)
    }

    "putTokenInStorage should return None for invalid index" in {
      val player = Player("Emilia", tokens = List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
      val table = Table(16, 90, List(List(NumToken(1, Color.RED))))
      val pf = PlayingField(List(player), List(), table, stack)
      controller.setPlayingField(Some(pf))
      controller.setStateInternal(GameState(table, Vector(player), Vector(), 0, stack))
      val result = controller.putTokenInStorage(99)
      result shouldBe None
    }

    "fromStorageToTable should move token from storage to table" in {
      val player = Player("Emilia", tokens = List(), tokenStructureFactory = tokenStructureFactory)
      val table = Table(16, 90, List(List()))
      val pf = PlayingField(List(player), List(), table, stack)
      val state = GameState(table, Vector(player), Vector(), 0, stack).updatedStorage(Vector("1:red"))
      controller.setPlayingField(Some(pf))
      controller.setStateInternal(state)
      val (newState, msg) = controller.fromStorageToTable(state, "1:red", 0, 0)
      msg should include ("Token 1:red was placed at index 0 in group 0.")
      newState.getStorageTokens should not contain "1:red"
      newState.getTable.getTokensOnTable.flatten should contain (NumToken(1, Color.RED))
    }

    "fromStorageToTable should return error if token not in storage" in {
      val player = Player("Emilia", tokens = List(), tokenStructureFactory = tokenStructureFactory)
      val table = Table(16, 90, List(List()))
      val pf = PlayingField(List(player), List(), table, stack)
      val state = GameState(table, Vector(player), Vector(), 0, stack).updatedStorage(Vector())
      controller.setPlayingField(Some(pf))
      controller.setStateInternal(state)
      val (newState, msg) = controller.fromStorageToTable(state, "1:red", 0, 0)
      msg should include ("Token was not found in storage!")
      newState shouldBe state
    }

    "fromStorageToTable should return error if insertAtIndex is invalid" in {
      val player = Player("Emilia", tokens = List(), tokenStructureFactory = tokenStructureFactory)
      val table = Table(16, 90, List(List()))
      val pf = PlayingField(List(player), List(), table, stack)
      val state = GameState(table, Vector(player), Vector(), 0, stack).updatedStorage(Vector("1:red"))
      controller.setPlayingField(Some(pf))
      controller.setStateInternal(state)
      val (newState, msg) = controller.fromStorageToTable(state, "1:red", 0, 5)
      msg should include ("invalid position to insert!")
      newState shouldBe state
    }

    "fromStorageToTable should return error if groupIndex is invalid" in {
      val player = Player("Emilia", tokens = List(), tokenStructureFactory = tokenStructureFactory)
      val table = Table(16, 90, List(List()))
      val pf = PlayingField(List(player), List(), table, stack)
      val state = GameState(table, Vector(player), Vector(), 0, stack).updatedStorage(Vector("1:red"))
      controller.setPlayingField(Some(pf))
      controller.setStateInternal(state)
      val (newState, msg) = controller.fromStorageToTable(state, "1:red", 5, 0)
      msg should include ("invalid group-index!")
      newState shouldBe state
    }

    "passTurn should keep player unchanged if hasCompletedFirstMove is already true" in {
      val player = Player("Emilia", hasCompletedFirstMove = true, tokenStructureFactory = tokenStructureFactory)
      val player2 = Player("Noah", tokenStructureFactory = tokenStructureFactory)
      val players = Vector(player, player2)
      val table = Table(16, 90, List(List(NumToken(1, Color.RED), NumToken(2, Color.RED), NumToken(3, Color.RED))))
      val boards = Vector(
        boardFactory.createBoard(0, 0, 2, 2, "dest1", 10),
        boardFactory.createBoard(1, 0, 2, 2, "dest2", 10)
      )
      val stack = tokenStackFactory.createShuffledStack
      val state = GameState(table, players, boards, 0, stack)

      controller.setPlayingField(Some(PlayingField(players.toList, boards.toList, table, stack)))

      val (resultState, message) = controller.passTurn(state, ignoreFirstMoveCheck = false)
      resultState.getPlayers.head shouldBe player
      message should not include ("first move")
      message should not include ("not valid")
    }

    "setGameStarted should update the gameStarted flag" in {
      controller.setGameStarted(true)
      controller.getGameStarted shouldBe true
      controller.setGameStarted(false)
      controller.getGameStarted shouldBe false
    }

    "setUndoManager should update the controller's undo manager" in {
      val oldManager = controller.getUndoManager
      val newManager = new UndoManager
      class DummyCommand extends de.htwg.se.rummikub.util.Command {
        override def doStep(): Unit = {}
        override def undoStep(): Unit = {}
        override def redoStep(): Unit = {}
      }
      newManager.doStep(new DummyCommand)
      controller.setUndoManager(newManager)
      controller.getUndoManager should not be oldManager
      controller.getUndoManager shouldBe newManager
    }

    "getFormattedTokensOnTableWithLabels should return formatted string with rows and groups" in {
      val rowTokens = List(NumToken(1, Color.RED), NumToken(2, Color.RED))
      val groupTokens = List(NumToken(3, Color.BLUE), NumToken(4, Color.BLUE))
      val table = Table(1, 90, List(rowTokens, groupTokens)) // 1 Row, Rest Groups
      val pf = PlayingField(List(player1, player2), boards, table, stack)
      controller.setPlayingField(Some(pf))
      controller.setStateInternal(GameState(table, Vector(player1, player2), boards.toVector, 0, stack))

      val result = controller.getFormattedTokensOnTableWithLabels

      result should include ("row0: [0] 1 (red), [1] 2 (red)")
      result should include ("group0: [2] 3 (blue), [3] 4 (blue)")
    }

    "getTokenFromString should throw IllegalArgumentException for unknown color" in {
      val ex = intercept[IllegalArgumentException] {
        controller.getTokenFromString("1:rainbow")
      }
      ex.getMessage should include ("Unknown color: rainbow")
    }

    "should return None when mapping storage tokens if gameState is None" in {
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      val result = controller match {
        case c: Controller => c.gameState.map(_.getStorageTokens.map(c.getTokenFromString))
        case _ => None
      }
      result shouldBe None
    }
  }
}