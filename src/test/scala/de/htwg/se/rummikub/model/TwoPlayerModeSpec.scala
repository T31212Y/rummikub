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

    "return None if createPlayingField is called with empty player list" in {
      val pf = mode.createPlayingField(Nil)
      pf shouldBe None
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

    "update the PlayingField and print error if less than 2 players" in {
      val pf = mode.createPlayingField(players)
      val pfWithOne = pf.map(f => f.copy(players = f.players.take(1)))
      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(out) {
        val updated = mode.updatePlayingField(pfWithOne)
        updated.isDefined shouldBe true
        updated.get.players.size shouldBe 1
      }
      out.toString should include ("Not enough players to update.")
    }

    "update a single board for a player" in {
      val player = Player("Azra")
      val board = new Board(24, 14, 2, 2, "default", 10)
      val updated = mode.updateBoardSinglePlayer(player, board)
      updated.isDefined shouldBe true
    }

    "update multiple boards for players returns None" in {
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

    "renderPlayingField returns error string if None" in {
      mode.renderPlayingField(None) should include ("No playing field available")
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
      val stack = new TokenStack(List.empty[Token])
      val manyTokensTuple = stack.drawMultipleTokens(30)
      val player = Player("Azra", manyTokensTuple._1)
      val board = new Board(24, 14, 2, 2, "default", 10)

      val updatedOpt = mode.updateBoardSinglePlayer(player, board)
      updatedOpt.isDefined shouldBe true

      val updatedBoard = updatedOpt.get
      updatedBoard.boardELRP12_1 shouldEqual board.formatBoardRow(manyTokensTuple._1.take(mode.cntTokens))
      updatedBoard.boardELRP12_2 shouldEqual board.formatBoardRow(manyTokensTuple._1.drop(mode.cntTokens))
      updatedBoard.boardEUD shouldEqual board.createBoardFrameSingle(manyTokensTuple._1.take(mode.cntTokens))
    }
  }
}