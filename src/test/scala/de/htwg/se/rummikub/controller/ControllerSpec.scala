package de.htwg.se.rummikub.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model._

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
  }
}