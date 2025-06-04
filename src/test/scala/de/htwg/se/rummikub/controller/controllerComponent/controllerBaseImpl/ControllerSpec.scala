package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.state.GameState

class ControllerSpec extends AnyWordSpec {

  "A Controller" should {

    val player1 = Player("Alice", List(NumToken(1, Color.RED)))
    val player2 = Player("Bob", List(NumToken(2, Color.BLUE)))
    val players = List(player1, player2)
    val boards = List(
      Board(0, 0, 2, 2, "dest1", 10),
      Board(1, 0, 2, 2, "dest2", 10)
    )
    val table = Table(16, 90, List())
    val stack = TokenStack(List(NumToken(3, Color.GREEN)))
    val pf = PlayingField(players, boards, table, stack)
    val gameMode = new GameModeTemplate(List("Alice", "Bob")) {
      override def runGameSetup(): Option[PlayingField] = Some(pf)
      override def renderPlayingField(field: Option[PlayingField]): String = "Spielfeld"
      override def createPlayingField(players: List[Player]): Option[PlayingField] = Some(pf)
      override def updatePlayingField(playingField: Option[PlayingField]): Option[PlayingField] = playingField
    }
    val controller = new Controller(gameMode)

    "setup a new game" in {
      controller.setupNewGame(2, List("Alice", "Bob"))
      controller.playingField.isDefined shouldBe true
      controller.gameState.isDefined shouldBe true
      controller.gameEnded shouldBe false
    }

    "start a game and distribute tokens" in {
      controller.setupNewGame(2, List("Alice", "Bob"))
      controller.startGame()
      controller.getState.players.foreach(_.tokens.size should be >= 1)
    }

    "create token stack, row and group" in {
      controller.createTokenStack() shouldBe a [TokenStack]
      controller.createRow(List(NumToken(1, Color.RED))) shouldBe a [Row]
      controller.createGroup(List(NumToken(1, Color.RED))) shouldBe a [Group]
    }

    "add and remove tokens to/from player" in {
      val (updatedPlayer, updatedStack) = controller.addTokenToPlayer(player1, stack)
      updatedPlayer.tokens.size shouldBe player1.tokens.size + 1
      updatedStack.tokens.size shouldBe stack.tokens.size - 1

      noException should be thrownBy controller.removeTokenFromPlayer(updatedPlayer, updatedPlayer.tokens.head)
    }

    "add multiple tokens to player" in {
      val (updatedPlayer, updatedStack) = controller.addMultipleTokensToPlayer(player1, stack, 1)
      updatedPlayer.tokens.size shouldBe player1.tokens.size + 1
      updatedStack.tokens.size shouldBe stack.tokens.size - 1
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
      val row = controller.createRow(List(NumToken(1, Color.RED)))
      val (rowTokens, updatedPlayer1) = controller.addRowToTable(row, player1)
      rowTokens should not be empty
      updatedPlayer1.tokens shouldBe empty

      val group = controller.createGroup(List(NumToken(2, Color.BLUE)))
      val (groupTokens, updatedPlayer2) = controller.addGroupToTable(group, player2)
      groupTokens should not be empty
      updatedPlayer2.tokens shouldBe empty
    }

    "support undo and redo" in {
      controller.setPlayingField(Some(pf))
      val row = controller.createRow(List(NumToken(1, Color.RED)))
      controller.executeAddRow(row, player1, stack)
      noException should be thrownBy controller.undo()
      noException should be thrownBy controller.redo()
    }

    "end the game" in {
      controller.endGame()
      controller.gameEnded shouldBe true
    }

    "not allow to play tokens that are not on the player's board" in {
      controller.setPlayingField(Some(pf))
      val fakeToken = NumToken(99, Color.BLACK)
      val row = controller.createRow(List(fakeToken))
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
      val group = controller.createGroup(List(NumToken(2, Color.BLUE)))
      controller.executeAddGroup(group, player2, stack)
      controller.getState.players.find(_.name == "Bob").get.tokens shouldBe empty
    }

    "throw NoSuchElementException if player is not found" in {
      val missingPlayer = Player("Charlie")
      val group = controller.createGroup(List(NumToken(2, Color.BLUE)))
      val exception = intercept[NoSuchElementException] {
        controller.executeAddGroup(group, missingPlayer, stack)
      }
      exception.getMessage should include ("Charlie")
    }

    "do nothing if undo is called with no previous state" in {
      noException should be thrownBy controller.undo()
      controller.getState shouldBe controller.getState
    }

    "do nothing if redo is called with no next state" in {
      noException should be thrownBy controller.redo()
      controller.getState shouldBe controller.getState
    }

    "return message if invalid token input is given" in {
      controller.setPlayingField(Some(pf))
      val invalidTokenString = List("foo")
      an [IllegalArgumentException] should be thrownBy {
        controller.addRowToTable(controller.createRow(controller.changeStringListToTokenList(List("foo"))), player1)
      }
    }

    "return message if first move requirement is not met when adding a group" in {
      controller.setPlayingField(Some(pf))
      val testPlayer = player1.copy(tokens = List(NumToken(5, Color.RED), NumToken(6, Color.RED), NumToken(7, Color.RED)))
      val groupTokens = List("5:red", "6:red", "7:red") 
      val (updatedPlayer, msg) = controller.playGroup(groupTokens, testPlayer, stack)
      msg should be ("Your move is not valid for the first move requirement.")
    }

    "passTurn should not allow passing if first move is not completed and ignoreFirstMoveCheck is false" in {
      val player = Player("Emilia", hasCompletedFirstMove = false)
      val player2 = Player("Noah")
      val players = Vector(player, player2)
      val table = Table(16, 90, List.empty)
      val boards = Vector.empty[Board]
      val stack = TokenStack()
      val state = GameState(table, players, boards, 0, stack)

      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      controller.playingField = Some(PlayingField(players.toList, boards.toList, table, stack))

      val (resultState, message) = controller.passTurn(state)
      resultState shouldBe state
      message shouldBe "The first move must have a total of at least 30 points. You cannot end your turn."
    }

    "winGame should return true and print winner if a player has no tokens" in {
      val winner = Player("Emilia", tokens = List())
      val loser = Player("Noah", tokens = List(NumToken(1, Color.RED)))
      val pf = PlayingField(List(winner, loser), List(), Table(0, 0, List()), TokenStack())
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      controller.playingField = Some(pf)

      val output = new java.io.ByteArrayOutputStream()
      Console.withOut(new java.io.PrintStream(output)) {
        controller.winGame() shouldBe true
      }
      output.toString should include ("Player Emilia wins the game!")
    }

    "winGame should return false if no player has won" in {
      val player1 = Player("Emilia", tokens = List(NumToken(1, Color.RED)))
      val player2 = Player("Noah", tokens = List(NumToken(2, Color.BLUE)))
      val pf = PlayingField(List(player1, player2), List(), Table(0, 0, List()), TokenStack())
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      controller.playingField = Some(pf)

      controller.winGame() shouldBe false
    }

    "winGame should return false if playingField is None" in {
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      controller.playingField = None

      controller.winGame() shouldBe false
    }

    "addGroupToTable should not allow playing tokens not on the player's board" in {
      val player = Player("Emilia", tokens = List(NumToken(1, Color.RED)))
      val token = NumToken(9, Color.BLACK)
      val group = Group(List(token))
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      controller.playingField = Some(PlayingField(List(player), List(), Table(0, 0, List()), TokenStack.apply()))

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
      val player = Player("Emilia", tokens = List(NumToken(1, Color.RED)))
      val player2 = Player("Noah")
      val table = Table(16, 90, List.empty)
      val boards = Vector.empty[Board]
      val stack = TokenStack.apply()
      val state = GameState(table, Vector(player, player2), boards, 0, stack)

      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      controller.playingField = Some(PlayingField(List(player), boards.toList, table, stack))
      controller.gameState = Some(state)
      controller.turnStartState = None

      val (resultState, message) = controller.drawFromStackAndPass

      resultState.players.head.tokens.nonEmpty shouldBe true
    }

    "playRow should return error if row is not valid" in {
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      val player = Player("Emilia")
      val stack = TokenStack.apply()
      val (resultPlayer, message) = controller.playRow(List("1:red"), player, stack)
      resultPlayer shouldBe player
      message shouldBe "Your move is not valid for the first move requirement."
    }

    "playRow should place a valid row and return success message" in {
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      val player = Player("Emilia", tokens = List(NumToken(10, Color.RED), NumToken(11, Color.RED), NumToken(12, Color.RED)))
      val stack = TokenStack.apply()
      controller.setPlayingField(Some(PlayingField(List(player), List(), Table(0, 0, List()), stack)))
      val (resultPlayer, message) = controller.playRow(List("10:red", "11:red", "12:red"), player, stack)
      message shouldBe "Row successfully placed."
      resultPlayer.hasCompletedFirstMove shouldBe true
    }

    "playGroup should return error if group is not valid" in {
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      val player = Player("Emilia")
      val stack = TokenStack.apply()
      val (resultPlayer, message) = controller.playGroup(List("1:red"), player, stack)
      resultPlayer shouldBe player
      message shouldBe "Your move is not valid for the first move requirement."
    }

    "playGroup should place a valid group and return success message" in {
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      val player = Player("Emilia", tokens = List(NumToken(10, Color.RED), NumToken(10, Color.BLACK), NumToken(10, Color.BLUE)))
      val stack = TokenStack.apply()
      controller.setPlayingField(Some(PlayingField(List(player), List(), Table(0, 0, List()), stack)))
      val (resultPlayer, message) = controller.playGroup(List("10:red", "10:black", "10:blue"), player, stack)
      message shouldBe "Group successfully placed."
      resultPlayer.hasCompletedFirstMove shouldBe true
    }

    "beginTurn should do nothing if commandHistory is not empty" in {
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      controller.turnStartState = None
      val oldUndoManager = controller.turnUndoManager

      val player = Player("Emilia", commandHistory = List("row:10:red,11:red,12:red"))
      controller.beginTurn(player)

      controller.turnStartState shouldBe None
      controller.turnUndoManager shouldBe oldUndoManager
    }
  }
}