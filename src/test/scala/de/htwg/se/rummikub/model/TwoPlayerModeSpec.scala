package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class TwoPlayerModeSpec extends AnyWordSpec {
  "A TwoPlayerMode" should {
    val playerNames = List("Azra", "Moritz")
    val mode = TwoPlayerMode(playerNames)
    val players = mode.createPlayers()

    "be created with two player names" in {
      mode.playerNames should be(playerNames)
    }

    "create two Player objects" in {
      players.size should be(2)
      players.map(_.name) should contain allElementsOf playerNames
    }

    "create a PlayingField with two players" in {
      val pf = mode.createPlayingField(players)
      pf.get.players.size should be(2)
      pf.get.players.map(_.name) should contain allElementsOf playerNames
    }

    "update the PlayingField and keep two boards" in {
      val pf = mode.createPlayingField(players)
      val updatedOpt = mode.updatePlayingField(pf)
      updatedOpt.isDefined shouldBe true
      updatedOpt.get.boards.size should be(2)
    }

    "update a single board for a player" in {
      val player = Player("Azra")
      val board = new Board(24, 14, 2, 2, "default", 10)
      val updated = mode.updateBoardSinglePlayer(player, board)
      updated.isDefined shouldBe true
    }

    "update multiple boards for players" in {
      val board = new Board(24, 14, 2, 2, "default", 10)
      val updated = mode.updateBoardMultiPlayer(players, board)
      updated shouldBe None
    }

    "render the PlayingField as a string" in {
      val pf = mode.createPlayingField(players)
      val str = mode.renderPlayingField(pf)
      str should include ("Azra")
      str should include ("Moritz")
    }

    "lineWithPlayerName should return a formatted line" in {
      val line = mode.lineWithPlayerName("*", 20, "Azra")
      line.isDefined shouldBe true
      line.get should include ("Azra")
    }

    "lineWith2PlayerNames should return None" in {
      mode.lineWith2PlayerNames("*", 20, "Azra", "Moritz") shouldBe None
    }
    "A player's board should split tokens correctly when more than cntTokens are present" in {
        val stack = new TokenStack()
        val manyTokens = stack.drawMultipleTokens(30)
        val player = Player("Azra", manyTokens)
        val board = new Board(24, 14, 2, 2, "default", 10)

        val updatedOpt = mode.updateBoardSinglePlayer(player, board)
        updatedOpt.isDefined shouldBe true

        val updatedBoard = updatedOpt.get
        updatedBoard.boardELRP12_1 shouldBe board.formatBoardRow(manyTokens.take(mode.cntTokens))
        updatedBoard.boardELRP12_2 shouldBe board.formatBoardRow(manyTokens.drop(mode.cntTokens))
        updatedBoard.boardEUD shouldBe board.createBoardFrameSingle(manyTokens.take(mode.cntTokens))
    }
  }
}