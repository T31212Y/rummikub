package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class FourPlayerModeSpec extends AnyWordSpec {
  "A FourPlayerMode" should {
    val playerNames = List("Anna", "Ben", "Clara", "David")
    val mode = FourPlayerMode(playerNames)
    val players = mode.createPlayers()

    "be created with four player names" in {
      mode.playerNames should be(playerNames)
    }

    "create four Player objects" in {
      players.size should be(4)
      players.map(_.name) should contain allElementsOf playerNames
    }

    "create a PlayingField with four players" in {
      val pfOpt = mode.createPlayingField(players)
      pfOpt.isDefined shouldBe true
      val pf = pfOpt.get
      pf.players.size should be(4)
      pf.players.map(_.name) should contain allElementsOf playerNames
    }

    "update the PlayingField and keep two boards" in {
      val pf = mode.createPlayingField(players)
      val updatedOpt = mode.updatePlayingField(pf)
      updatedOpt.isDefined shouldBe true
      val updated = updatedOpt.get
      updated.boards.size should be(2)
    }

    "update multiple boards for players (both have > cntTokens)" in {
      val tokens0 = (1 to 30).map(i => NumToken(i, Color.RED)).toList
      val tokens1 = (1 to 30).map(i => NumToken(i, Color.BLUE)).toList

      val player0 = Player("Anna", tokens0)
      val player1 = Player("Ben", tokens1)

      val board = new Board(24, 14, 2, 2, "default", 10)
      val updatedOpt = mode.updateBoardMultiPlayer(List(player0, player1), board)

      updatedOpt.isDefined shouldBe true
      val updatedBoard = updatedOpt.get

      updatedBoard.boardELRP12_1 shouldBe board.formatBoardRow(tokens0.take(mode.cntTokens))
      updatedBoard.boardELRP12_2 shouldBe board.formatBoardRow(tokens0.drop(mode.cntTokens))
      updatedBoard.boardELRP34_1 shouldBe board.formatBoardRow(tokens1.take(mode.cntTokens))
      updatedBoard.boardELRP34_2 shouldBe board.formatBoardRow(tokens1.drop(mode.cntTokens))
    }

    "update a single board for a player (should return None)" in {
      val player = Player("Anna")
      val board = new Board(15, 24, 2, 1, "up")
      val updated = mode.updateBoardSinglePlayer(player, board)
      updated shouldBe None
    }

    "render the PlayingField as a string" in {
      val pf = mode.createPlayingField(players)
      val str = mode.renderPlayingField(pf)
      str should include ("Anna")
      str should include ("Ben")
      str should include ("Clara")
      str should include ("David")
    }

    "lineWith2PlayerNames should return a formatted line" in {
      val line = mode.lineWith2PlayerNames("*", 30, "Anna", "David")
      line.isDefined shouldBe true
      line.get should include ("Anna")
      line.get should include ("David")
    }

    "lineWithPlayerName should return None" in {
      mode.lineWithPlayerName("*", 20, "Anna") shouldBe None
    }


    "return None when creating a playing field with empty player list" in {
      val pfOpt = mode.createPlayingField(Nil)
      pfOpt shouldBe None
    }

    "return the same field if updatePlayingField is called with less than 4 players" in {
      val pf = mode.createPlayingField(players.take(2))
      val updatedOpt = mode.updatePlayingField(pf)
      updatedOpt.isDefined shouldBe true
      updatedOpt.get.players.size shouldBe 2
    }

    "renderPlayingField should return a message if None is given" in {
      mode.renderPlayingField(None) should include ("No playing field available")
    }
  }
}