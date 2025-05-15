package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class ThreePlayerModeSpec extends AnyWordSpec {
  "A ThreePlayerMode" should {
    val playerNames = List("Alice", "Bob", "Charlie")
    val mode = ThreePlayerMode(playerNames)
    val players = mode.createPlayers()

    "be created with three player names" in {
      mode.playerNames should be(playerNames)
    }

    "create three Player objects" in {
      players.size should be(3)
      players.map(_.name) should contain allElementsOf playerNames
    }

    "create a PlayingField with three players" in {
      val pf = mode.createPlayingField(players)
      pf.players.size should be(3)
      pf.players.map(_.name) should contain allElementsOf playerNames
    }

    "update the PlayingField and keep two boards" in {
      val pf = mode.createPlayingField(players)
      val updated = mode.updatePlayingField(pf)
      updated.boards.size should be(2)
    }

    "update a single board for a player" in {
      val player = Player("Alice")
      val board = new Board(24, 14, 2, 2, "default", 10)
      val updated = mode.updateBoardSinglePlayer(player, board)
      updated.isDefined shouldBe true
    }

    "update multiple boards for players (player 0 has > cntTokens)" in {
      val tokens0 = (1 to 30).map(i => NumToken(i, Color.RED)).toList
      val tokens1 = (1 to 10).map(i => NumToken(i, Color.BLUE)).toList

      val player0 = Player("Alice", tokens0)
      val player1 = Player("Bob", tokens1)

      val board = new Board(24, 14, 2, 2, "default", 10)
      val updatedOpt = mode.updateBoardMultiPlayer(List(player0, player1), board)

      updatedOpt.isDefined shouldBe true
      val updatedBoard = updatedOpt.get

      updatedBoard.boardELRP12_1 shouldBe board.formatBoardRow(tokens0.take(mode.cntTokens))
      updatedBoard.boardELRP12_2 shouldBe board.formatBoardRow(tokens0.drop(mode.cntTokens))
    }

    "update multiple boards for players (player 1 has > cntTokens)" in {
      val tokens0 = (1 to 10).map(i => NumToken(i, Color.RED)).toList
      val tokens1 = (1 to 30).map(i => NumToken(i, Color.BLUE)).toList

      val player0 = Player("Alice", tokens0)
      val player1 = Player("Bob", tokens1)

      val board = new Board(24, 14, 2, 2, "default", 10)
      val updatedOpt = mode.updateBoardMultiPlayer(List(player0, player1), board)

      updatedOpt.isDefined shouldBe true
      val updatedBoard = updatedOpt.get

      updatedBoard.boardELRP34_1 shouldBe board.formatBoardRow(tokens1.take(mode.cntTokens))
      updatedBoard.boardELRP34_2 shouldBe board.formatBoardRow(tokens1.drop(mode.cntTokens))
    }

    "update multiple boards for players (both have > cntTokens)" in {
      val tokens0 = (1 to 30).map(i => NumToken(i, Color.RED)).toList
      val tokens1 = (1 to 30).map(i => NumToken(i, Color.BLUE)).toList

      val player0 = Player("Alice", tokens0)
      val player1 = Player("Bob", tokens1)

      val board = new Board(24, 14, 2, 2, "default", 10)
      val updatedOpt = mode.updateBoardMultiPlayer(List(player0, player1), board)

      updatedOpt.isDefined shouldBe true
      val updatedBoard = updatedOpt.get

      updatedBoard.boardELRP12_1 shouldBe board.formatBoardRow(tokens0.take(mode.cntTokens))
      updatedBoard.boardELRP12_2 shouldBe board.formatBoardRow(tokens0.drop(mode.cntTokens))
      updatedBoard.boardELRP34_1 shouldBe board.formatBoardRow(tokens1.take(mode.cntTokens))
      updatedBoard.boardELRP34_2 shouldBe board.formatBoardRow(tokens1.drop(mode.cntTokens))
    }

    "render the PlayingField as a string" in {
      val pf = mode.createPlayingField(players)
      val str = mode.renderPlayingField(pf)
      str should include ("Alice")
      str should include ("Bob")
      str should include ("Charlie")
    }

    "lineWithPlayerName should return a formatted line" in {
      val line = mode.lineWithPlayerName("*", 20, "Alice")
      line.isDefined shouldBe true
      line.get should include ("Alice")
    }

    "lineWith2PlayerNames should return a formatted line" in {
      val line = mode.lineWith2PlayerNames("*", 30, "Alice", "Charlie")
      line.isDefined shouldBe true
      line.get should include ("Alice")
      line.get should include ("Charlie")
    }
    "A player's board should split tokens correctly when more than cntTokens are present" in {
        val stack = new TokenStack()
        val manyTokens = stack.drawMultipleTokens(30)
        val player = Player("Azra", manyTokens)
        val board = new Board(24, 14, 3, 1, "down")

        val updatedOpt = mode.updateBoardSinglePlayer(player, board)
        updatedOpt.isDefined shouldBe true

        val updatedBoard = updatedOpt.get
        updatedBoard.boardELRP12_1 shouldBe board.formatBoardRow(manyTokens.take(mode.cntTokens))
        updatedBoard.boardELRP12_2 shouldBe board.formatBoardRow(manyTokens.drop(mode.cntTokens))
        updatedBoard.boardEUD shouldBe board.createBoardFrameSingle(manyTokens.take(mode.cntTokens))
    }

  }
}