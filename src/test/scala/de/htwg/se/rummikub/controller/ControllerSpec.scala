package de.htwg.se.rummikub.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.util.TokenUtils.tokensMatch
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

class ControllerSpec extends AnyWordSpec {
  "A Controller" should {
    "detect win if player has no tokens" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah")
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      val pf = controller.playingField.get.copy(players = List(player1.copy(tokens = Nil), player2.copy(tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))))
      controller.playingField = Some(pf)
      controller.winGame() shouldBe true
    }

    "not detect win when no player won yet" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      val pf = controller.playingField.get.copy(players = List(player1.copy(tokens = List(NumToken(1, Color.RED))), player2.copy(tokens = List(NumToken(2, Color.BLUE)))))
      controller.playingField = Some(pf)
      controller.winGame() shouldBe false
    }

    "set a new playing field" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      controller.playingField.get.players.map(_.name) should contain allElementsOf List("Emilia", "Noah")
    }

    "convert the playing field to a string" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      controller.playingfieldToString should include ("Emilia")
      controller.playingfieldToString should include ("Noah")
    }

    "add a token to a player" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))
      val stack = TokenStack()

      controller.addTokenToPlayer(player1, stack)
      controller.playingField.get.players.find(_.name == "Emilia").get.tokens.size shouldBe 1
      stack.tokens.size shouldBe 105
    }

    "keep the correct first player in playingField.players.head after adding a token" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))
      val stack = TokenStack()

      controller.addTokenToPlayer(player1, stack)
      controller.playingField.get.players.head.name shouldBe "Emilia"
      controller.playingField.get.players.head.tokens.nonEmpty shouldBe true
    }

    "remove a token from a player" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      val token = NumToken(1, Color.RED)
      val p = player1.copy(tokens = List(token))
      controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
      controller.removeTokenFromPlayer(p, token)
      controller.playingField.get.players.find(_.name == "Emilia").get.tokens should not contain token
    }

    "add multiple tokens to a player" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))
      val stack = TokenStack()

      controller.addMultipleTokensToPlayer(player1, stack, 2)
      controller.playingField.get.players.find(_.name == "Emilia").get.tokens.size shouldBe 2
      stack.tokens.size shouldBe 104
    }

    "add a row to the table" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      val row = Row(List("1:red", "2:red", "3:red"))

      val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(2, Color.RED), NumToken(3, Color.RED)))
      controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
      val added = controller.addRowToTable(row, p)
      val (tokens, player) = added
      tokens should contain allOf (NumToken(1, Color.RED), NumToken(2, Color.RED), NumToken(3, Color.RED))
    }

    "not add a row if tokens are not on the player's board" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      val row = Row(List("9:red", "10:red", "11:red"))

      val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(2, Color.RED)))
      controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
      val added = controller.addRowToTable(row, p)
      val (tokens, player) = added
      tokens shouldBe empty

    }

    "add a group to the table" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      val group = Group(List("1:red", "1:blue", "1:black"))

      val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(1, Color.BLUE), NumToken(1, Color.BLACK)))
      controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
      val added = controller.addGroupToTable(group, p)
      val (tokens, player) = added
      tokens should contain allOf (NumToken(1, Color.RED), NumToken(1, Color.BLUE), NumToken(1, Color.BLACK))
    }

    "not add a group if tokens are not on the player's board" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      val group = Group(List("9:red", "9:blue", "9:black"))

      val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
      val added = controller.addGroupToTable(group, p)
      val (tokens, player) = added
      tokens shouldBe empty
    }

    "process game input for 'draw'" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))
      val stack = TokenStack()

      val p = player1.copy(tokens = Nil, commandHistory = Nil)
      controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
      val nextPlayer = controller.processGameInput("draw", p, stack)
      nextPlayer.name shouldBe "Noah"
      controller.playingField.get.players.find(_.name == "Emilia").get.tokens.size shouldBe 1
      stack.tokens.size shouldBe 105
    }

    "process invalid game input" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))
      val stack = TokenStack()

      val p = player1.copy(tokens = Nil)
      controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
      val nextPlayer = controller.processGameInput("invalid", p, stack)
      nextPlayer.name shouldBe "Emilia"
    }

    "process game input for 'row' (invalid)" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))
      val input = new ByteArrayInputStream("1:red,2:red,3:red\n".getBytes)

      Console.withIn(input) {
        val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(2, Color.RED), NumToken(3, Color.RED)))
        controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
        controller.processGameInput("row", p, TokenStack())
        controller.validFirstMoveThisTurn should be(false)
      }
    }

    "process game input for 'row' (valid)" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))
      val input = new ByteArrayInputStream("10:red,11:red,12:red\n".getBytes)

      Console.withIn(input) {
        val p = player1.copy(tokens = List(NumToken(10, Color.RED), NumToken(11, Color.RED), NumToken(12, Color.RED)))
        controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
        controller.processGameInput("row", p, TokenStack())
        controller.validFirstMoveThisTurn should be(true)
      }
    }

    "process game input for 'group' (invalid)" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))
      val input = new ByteArrayInputStream("1:red,1:blue,1:black\n".getBytes)

      Console.withIn(input) {
        val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(1, Color.BLUE), NumToken(1, Color.BLACK)))
        controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
        controller.processGameInput("group", p, TokenStack())
        controller.validFirstMoveThisTurn should be(false)
      }
    }

    "process game input for 'group' (valid)" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))
      val input = new ByteArrayInputStream("10:red,10:blue,10:black\n".getBytes)

      Console.withIn(input) {
        val p = player1.copy(tokens = List(NumToken(10, Color.RED), NumToken(10, Color.BLUE), NumToken(10, Color.BLACK)))
        controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
        controller.processGameInput("group", p, TokenStack())
        controller.validFirstMoveThisTurn should be(true)
      }
    }

    "setup a new game" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      
      controller.setupNewGame(2, List("Anna", "Ben"))
      controller.playingField.get.players.map(_.name) shouldBe List("Anna", "Ben")
      controller.playingField.get.players.map(_.name) shouldBe List("Anna", "Ben")
    }

    "set the next player" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      val nextPlayer = controller.setNextPlayer(player1)
      nextPlayer.name shouldBe "Noah"
    }

    "set the first player after reaching the last player" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      val nextPlayer = controller.setNextPlayer(player2)
      nextPlayer.name shouldBe "Emilia"
    }

    "parse token string to integer" in {
      val tokenString = "10:red"
      val parts = tokenString.split(":")
      val result = if (parts(0).forall(_.isDigit)) parts(0).toInt else 0
      result shouldBe 10
    }

    "create a Row from a list of strings" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      val tokensList = List("1:red", "2:blue", "3:green")
      val row = controller.createRow(tokensList)
      row shouldBe a[Row]
      row.getTokens.map(_.toString) should be (List("\u001b[31m 1\u001b[0m", "\u001b[34m 2\u001b[0m", "\u001b[32m 3\u001b[0m"))
    }
  
    "create a Group from a list of strings" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)

      val tokensList = List("4:red", "4:blue", "4:green")
      val group = controller.createGroup(tokensList)
      group shouldBe a[Group]
      group.getTokens.map(_.toString) should be (List("\u001b[31m 4\u001b[0m", "\u001b[34m 4\u001b[0m", "\u001b[32m 4\u001b[0m"))
    }

    "not allow to pass turn if no token was played (commandHistory is empty)" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      val p = player1.copy(commandHistory = Nil)
      controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
      val outStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outStream)) {
        val resultPlayer = controller.passTurn(p, false)
        resultPlayer.name shouldBe "Emilia"
      }
      outStream.toString should include ("The first move must have a total of at least 30 points. You cannot end your turn.")
    }

    "pass turn to next player if draw is called after playing at least one token but less than 30 points" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))
      val stack = TokenStack()

      val p = player1.copy(
        tokens = Nil,
        commandHistory = List("row:10:red,10:blue")
      )
      controller.playingField = Some(controller.playingField.get.copy(players = List(p, player2)))
      val nextPlayer = controller.processGameInput("draw", p, stack)
      nextPlayer.name shouldBe "Noah"
    }

    "match two Joker tokens as equal in tokensMatch" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)

      val joker1 = Joker(Color.RED)
      val joker2 = Joker(Color.BLUE)
      tokensMatch(joker1, joker2) shouldBe true
    }

    "not match different token types in tokensMatch" in {
      val player1 = Player("Emilia")
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)

      val joker = Joker(Color.RED)
      val numToken = NumToken(1, Color.RED)
      tokensMatch(joker, numToken) shouldBe false
    }

    "not allow turn to pass if validateFirstMove is false" in {
      val player1 = Player("Emilia", tokens = List(NumToken(5, Color.RED))).copy(commandHistory = List("row:5:red"))
      val player2 = Player("Noah", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      val players = List(player1, player2)
      val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)).get)
      controller.setupNewGame(2, players.map(_.name))

      assert(player1.validateFirstMove() == false)

      val result = controller.passTurn(player1, allowWithoutFirstMove = false)

      result shouldBe player1
    }

    "pass the turn to the next player when 'pass' is entered and the conditions are met" in {
      val player1 = Player("Emilia", tokens = List(NumToken(1, Color.RED)), commandHistory = List("row:10:red,10:blue,10:green"), firstMoveTokens = List(NumToken(11, Color.RED), NumToken(12, Color.BLUE), NumToken(13, Color.GREEN)))
      val player2 = Player("Noah", tokens = List(NumToken(2, Color.BLUE)))
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      
      controller.setupNewGame(2, List("Emilia", "Noah"))
      controller.playingField = Some(controller.playingField.get.copy(players = List(player1, player2)))
      controller.validFirstMoveThisTurn = true
      
      val stack = TokenStack()
      val resultPlayer = controller.processGameInput("pass", player1, stack)
      
      resultPlayer.name shouldBe "Noah"
    }

    "passTurn should switch to next player and reset commandHistory if first move is valid" in {
      val playerNames = List("Emilia", "Noah")
      val controller = new Controller(GameModeFactory.createGameMode(2, playerNames).get)
      controller.setupNewGame(2, playerNames)

      val pf = controller.playingField.get
      val emilia = pf.players.head.copy(
        tokens = List(NumToken(10, Color.RED), NumToken(10, Color.BLUE), NumToken(10, Color.GREEN)),
        commandHistory = List("row:10:red,10:blue,10:green")
      )
      val noah = pf.players(1)
      controller.playingField = Some(pf.copy(players = List(emilia, noah)))
      controller.currentPlayerIndex = 0

      val emiliaFromField = controller.playingField.get.players.head

      val outStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outStream)) {
        val result = controller.passTurn(emiliaFromField)
        result.name shouldBe "Noah"
        result.commandHistory shouldBe empty
        controller.currentPlayerIndex shouldBe 1
      }
    }

    "setNextPlayer should wrap around to first player" in {
      val playerNames = List("Emilia", "Noah")
      val controller = new Controller(GameModeFactory.createGameMode(2, playerNames).get)
      controller.setupNewGame(2, playerNames)
      controller.currentPlayerIndex = 1
      val pf = controller.playingField.get
      val nextPlayer = controller.setNextPlayer(pf.players(1))
      nextPlayer.name shouldBe "Emilia"
      controller.currentPlayerIndex shouldBe 0
    }

    "undo and redo should not throw" in {
      val playerNames = List("Emilia", "Noah")
      val controller = new Controller(GameModeFactory.createGameMode(2, playerNames).get)
      controller.setupNewGame(2, playerNames)
      noException should be thrownBy controller.undo()
      noException should be thrownBy controller.redo()
    }

    "addTokenToPlayer should add a token" in {
      val playerNames = List("Emilia", "Noah")
      val controller = new Controller(GameModeFactory.createGameMode(2, playerNames).get)
      controller.setupNewGame(2, playerNames)
      val pf = controller.playingField.get
      val player = pf.players.head
      val stack = TokenStack()
      controller.addTokenToPlayer(player, stack)
      controller.playingField.get.players.find(_.name == "Emilia").get.tokens.size shouldBe 1
    }

    "removeTokenFromPlayer should remove a token" in {
      val playerNames = List("Emilia", "Noah")
      val controller = new Controller(GameModeFactory.createGameMode(2, playerNames).get)
      controller.setupNewGame(2, playerNames)
      val pf = controller.playingField.get
      val token = NumToken(1, Color.RED)
      val player = pf.players.head.copy(tokens = List(token))
      controller.playingField = Some(pf.copy(players = List(player, pf.players(1))))
      controller.removeTokenFromPlayer(player, token)
      controller.playingField.get.players.find(_.name == "Emilia").get.tokens should not contain token
    }

    "addMultipleTokensToPlayer should add multiple tokens" in {
      val playerNames = List("Emilia", "Noah")
      val controller = new Controller(GameModeFactory.createGameMode(2, playerNames).get)
      controller.setupNewGame(2, playerNames)
      val pf = controller.playingField.get
      val player = pf.players.head
      val stack = TokenStack()
      controller.addMultipleTokensToPlayer(player, stack, 3)
      controller.playingField.get.players.find(_.name == "Emilia").get.tokens.size shouldBe 3
    }

    "winGame should return true if a player has no tokens" in {
      val playerNames = List("Emilia", "Noah")
      val controller = new Controller(GameModeFactory.createGameMode(2, playerNames).get)
      controller.setupNewGame(2, playerNames)
      val pf = controller.playingField.get
      val player1 = pf.players.head.copy(tokens = Nil)
      val player2 = pf.players(1).copy(tokens = List(NumToken(1, Color.RED)))
      controller.playingField = Some(pf.copy(players = List(player1, player2)))
      controller.winGame() shouldBe true
    }

    "winGame should return false if no player has won" in {
      val playerNames = List("Emilia", "Noah")
      val controller = new Controller(GameModeFactory.createGameMode(2, playerNames).get)
      controller.setupNewGame(2, playerNames)
      val pf = controller.playingField.get

      val player1 = pf.players.head.copy(tokens = List(NumToken(1, Color.RED)))
      val player2 = pf.players(1).copy(tokens = List(NumToken(2, Color.BLUE)))
      controller.playingField = Some(pf.copy(players = List(player1, player2)))
      controller.winGame() shouldBe false
    }

    "processGameInput should handle unknown command" in {
      val playerNames = List("Emilia", "Noah")
      val controller = new Controller(GameModeFactory.createGameMode(2, playerNames).get)
      controller.setupNewGame(2, playerNames)
      val pf = controller.playingField.get
      val player = pf.players.head
      val stack = TokenStack()
      val result = controller.processGameInput("foobar", player, stack)
      result shouldBe player
    }
  }
}