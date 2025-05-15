package de.htwg.se.rummikub.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model._
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

class ControllerSpec extends AnyWordSpec {
  "A Controller" should {
    val player1 = Player("Emilia")
    val player2 = Player("Noah")
    val players = List(player1, player2)
    val controller = new Controller(GameModeFactory.createGameMode(2, players.map(_.name)))
    controller.playingField = controller.gameMode.createPlayingField(players)

    "detect win if player has no tokens" in {
      val pf = controller.playingField.copy(players = List(player1.copy(tokens = Nil), player2))
      controller.playingField = pf
      controller.winGame() shouldBe true
    }

    "not detect win when no player won yet" in {
      val pf = controller.playingField.copy(players = List(player1.copy(tokens = List(NumToken(1, Color.RED))), player2.copy(tokens = List(NumToken(2, Color.BLUE)))))
      controller.playingField = pf
      controller.winGame() shouldBe false
    }

    "pass turn to next player" in {
      val nextPlayer = controller.passTurn(player1)
      println(s"${player1.name} ended their turn. It's now ${nextPlayer.name}'s turn.")
      nextPlayer.name shouldBe "Noah"
    }

    "set a new playing field" in {
      val pf = controller.gameMode.createPlayingField(players)
      controller.setPlayingField(pf)
      controller.playingField.players.map(_.name) should contain allElementsOf List("Emilia", "Noah")
    }

    "convert the playing field to a string" in {
      controller.playingfieldToString should include ("Emilia")
      controller.playingfieldToString should include ("Noah")
    }

    "add a token to a player" in {
      val stack = TokenStack()
      controller.addTokenToPlayer(player1, stack)
      controller.playingField.players.find(_.name == "Emilia").get.tokens.size shouldBe 1
      stack.tokens.size shouldBe 105
    }

    "keep the correct first player in playingField.players.head after adding a token" in {
      val stack = TokenStack()
      controller.addTokenToPlayer(player1, stack)
      controller.playingField.players.head.name shouldBe "Emilia"
      controller.playingField.players.head.tokens.nonEmpty shouldBe true
    }

    "remove a token from a player" in {
      val token = NumToken(1, Color.RED)
      val p = player1.copy(tokens = List(token))
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      controller.removeTokenFromPlayer(p, token)
      controller.playingField.players.find(_.name == "Emilia").get.tokens should not contain token
    }

    "add multiple tokens to a player" in {
      val stack = TokenStack()
      controller.addMultipleTokensToPlayer(player1, stack, 2)
      controller.playingField.players.find(_.name == "Emilia").get.tokens.size shouldBe 2
      stack.tokens.size shouldBe 104
    }

    "add a row to the table" in {
      val row = Row(List("1:red", "2:red", "3:red"))
      val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(2, Color.RED), NumToken(3, Color.RED)))
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val added = controller.addRowToTable(row, p)
      added should contain allOf (NumToken(1, Color.RED), NumToken(2, Color.RED), NumToken(3, Color.RED))
    }

    "not add a row if tokens are not on the player's board" in {
      val row = Row(List("9:red", "10:red", "11:red"))
      val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(2, Color.RED)))
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val added = controller.addRowToTable(row, p)
      added shouldBe empty
    }

    "add a group to the table" in {
      val group = Group(List("1:red", "1:blue", "1:black"))
      val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(1, Color.BLUE), NumToken(1, Color.BLACK)))
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val added = controller.addGroupToTable(group, p)
      added should contain allOf (NumToken(1, Color.RED), NumToken(1, Color.BLUE), NumToken(1, Color.BLACK))
    }

    "not add a group if tokens are not on the player's board" in {
      val group = Group(List("9:red", "9:blue", "9:black"))
      val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val added = controller.addGroupToTable(group, p)
      added shouldBe empty
    }

    "process game input for 'draw'" in {
      val stack = TokenStack()
      val p = player1.copy(tokens = Nil, commandHistory = Nil)
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val nextPlayer = controller.processGameInput("draw", p, stack)
      nextPlayer.name shouldBe "Noah"
      controller.playingField.players.find(_.name == "Emilia").get.tokens.size shouldBe 1
      stack.tokens.size shouldBe 105
    }

    "process game input for 'pass' (valid)" in {
      val tokenFactory = new StandardTokenFactory
      val p = player1.copy(tokens = List(tokenFactory.createNumToken(10, Color.RED)), commandHistory = List("row:10:red,10:blue,10:green"))
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val nextPlayer = controller.processGameInput("pass", p, TokenStack())
      nextPlayer.name shouldBe "Noah"
    }

    "process game input for 'pass' (invalid)" in {
      val stack = TokenStack()
      val p = player1.copy(tokens = Nil, commandHistory = Nil)
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val outStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outStream)) {
        val nextPlayer = controller.processGameInput("pass", p, stack)
        nextPlayer.name shouldBe "Emilia"
      }
      outStream.toString should include ("You cannot pass your turn without making a valid first move of at least 30 points.")
    }

    "process invalid game input" in {
      val stack = TokenStack()
      val p = player1.copy(tokens = Nil)
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val nextPlayer = controller.processGameInput("invalid", p, stack)
      nextPlayer.name shouldBe "Emilia"
    }

    "process game input for 'row'" in {
      val input = new ByteArrayInputStream("1:red,2:red,3:red\n".getBytes)
      Console.withIn(input) {
        val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(2, Color.RED), NumToken(3, Color.RED)))
        controller.playingField = controller.playingField.copy(players = List(p, player2))
        val nextPlayer = controller.processGameInput("row", p, TokenStack())
        nextPlayer.name shouldBe "Emilia"
        controller.playingField.players.find(_.name == "Emilia").get.tokens shouldBe empty
      }
    }

    "process game input for 'group'" in {
      val input = new ByteArrayInputStream("1:red,1:blue,1:black\n".getBytes)
      Console.withIn(input) {
        val p = player1.copy(tokens = List(NumToken(1, Color.RED), NumToken(1, Color.BLUE), NumToken(1, Color.BLACK)))
        controller.playingField = controller.playingField.copy(players = List(p, player2))
        val nextPlayer = controller.processGameInput("group", p, TokenStack())
        nextPlayer.name shouldBe "Emilia"
        controller.playingField.players.find(_.name == "Emilia").get.tokens shouldBe empty
      }
    }

    "setup a new game" in {
      controller.setupNewGame(2, List("Anna", "Ben"))
      controller.playingField.players.map(_.name) shouldBe List("Anna", "Ben")
      controller.playingField.players.map(_.name) shouldBe List("Anna", "Ben")
    }

    "set the next player" in {
      val nextPlayer = controller.setNextPlayer(player1)
      nextPlayer.name shouldBe "Noah"
    }

    "set the first player after reaching the last player" in {
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
        val tokensList = List("1:red", "2:blue", "3:green")
        val row = controller.createRow(tokensList)
        row shouldBe a[Row]
        row.rowTokens.map(_.toString) should contain allElementsOf tokensList
    }
  
    "create a Group from a list of strings" in {
        val tokensList = List("4:red", "4:blue", "4:yellow")
        val group = controller.createGroup(tokensList)
        group shouldBe a[Group]
        group.groupTokens.map(_.toString) should contain allElementsOf tokensList
    }

    "not allow to end turn if no token was played (commandHistory is empty)" in {
      val p = player1.copy(commandHistory = Nil)
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val outStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outStream)) {
        val resultPlayer = controller.endTurn(p)
        resultPlayer.name shouldBe "Emilia"
      }
      outStream.toString should include ("You must play at least one token before ending your turn.")
    }

    "allow to end turn if at least one token was played and 30 points erreicht wurden" in {
      val p = player1.copy(commandHistory = List("row:10:red,10:blue,10:green"))
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val outStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outStream)) {
        val resultPlayer = controller.endTurn(p)
        resultPlayer.name shouldBe "Noah"
      }
      outStream.toString should include ("Emilia ended their turn. It's now Noah's turn.")
    }

    "not allow to draw a token after making a valid first move" in {
      val stack = TokenStack()
      val p = player1.copy(
        tokens = Nil,
        commandHistory = List("row:10:red,10:blue,10:green") 
      )
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val outStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outStream)) {
        println("You cannot draw a token after making a valid first move.")
        val nextPlayer = controller.processGameInput("draw", p, stack)
        nextPlayer.name shouldBe "Emilia" 
      }
      outStream.toString should include ("You cannot draw a token after making a valid first move.")
    }

    "pass turn to next player if draw is called after playing at least one token but less than 30 points" in {
      val stack = TokenStack()
      val p = player1.copy(
        tokens = Nil,
        commandHistory = List("row:10:red,10:blue")
      )
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val nextPlayer = controller.processGameInput("draw", p, stack)
      nextPlayer.name shouldBe "Noah"
    }

    "pass turn to next player if pass is called and first move is valid" in {
      val p = player1.copy(
        tokens = Nil,
        commandHistory = List("row:10:red,10:blue,10:green")
      )
      controller.playingField = controller.playingField.copy(players = List(p, player2))
      val outStream = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outStream)) {
        val nextPlayer = controller.processGameInput("pass", p, TokenStack())
        nextPlayer.name shouldBe "Noah"
      }
    }

    "match two Joker tokens as equal in tokensMatch" in {
      val joker1 = Joker(Color.RED)
      val joker2 = Joker(Color.BLUE)
      controller.tokensMatch(joker1, joker2) shouldBe true
    }

    "not match different token types in tokensMatch" in {
      val joker = Joker(Color.RED)
      val numToken = NumToken(1, Color.RED)
      controller.tokensMatch(joker, numToken) shouldBe false
    }
  }
}
