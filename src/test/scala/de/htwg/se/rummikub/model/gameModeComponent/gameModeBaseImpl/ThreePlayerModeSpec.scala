package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Table
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Board
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.TokenStack
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.Color
import de.htwg.se.rummikub.model.builderComponent.FieldDirectorInterface

import de.htwg.se.rummikub.RummikubDependencyModule.given
import de.htwg.se.rummikub.RummikubDependencyModule.ThreePlayerTag

class ThreePlayerModeSpec extends AnyWordSpec {
  "A ThreePlayerMode" should {
    val playerNames = List("Alice", "Bob", "Charlie")
    val director = summon[FieldDirectorInterface & ThreePlayerTag]
    val mode = ThreePlayerMode(playerNames)(using tokenStackFactory, tableFactory, boardFactory, playerFactory, playingFieldBuilder, director)
    val players = mode.createPlayers

    "be created with three player names" in {
      mode.playerNames should be(playerNames)
    }

    "create three Player objects" in {
      players.size should be(3)
      players.map(_.getName) should contain allElementsOf playerNames
    }

    "create a PlayingField with three players" in {
      val pfOpt = mode.createPlayingField(players)
      pfOpt.isDefined shouldBe true
      val pf = pfOpt.get
      pf.getPlayers.size should be(3)
      pf.getPlayers.map(_.getName) should contain allElementsOf playerNames
    }

    "return None if createPlayingField is called with empty player list" in {
      mode.createPlayingField(Nil) shouldBe None
    }

    "update the PlayingField and keep two boards" in {
      val pf = mode.createPlayingField(players)
      val updatedOpt = mode.updatePlayingField(pf)
      updatedOpt.isDefined shouldBe true
      val updated = updatedOpt.get
      updated.getBoards.size should be(2)
    }

    "update the PlayingField and print error if less than 3 players" in {
      val pf = mode.createPlayingField(players)
      val pfWithTwo = pf.map(f => f.updated(newPlayers = f.getPlayers.take(2), newBoards = f.getBoards, newInnerField = f.getInnerField))
      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(out) {
        val updated = mode.updatePlayingField(pfWithTwo)
        updated.isDefined shouldBe true
        updated.get.getPlayers.size shouldBe 2
      }
      out.toString should include ("Not enough players to update.")
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

      updatedBoard.getBoardELRP12_1 shouldBe board.formatBoardRow(tokens0.take(mode.cntTokens))
      updatedBoard.getBoardELRP12_2 shouldBe board.formatBoardRow(tokens0.drop(mode.cntTokens))
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

      updatedBoard.getBoardELRP34_1 shouldBe board.formatBoardRow(tokens1.take(mode.cntTokens))
      updatedBoard.getBoardELRP34_2 shouldBe board.formatBoardRow(tokens1.drop(mode.cntTokens))
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

      updatedBoard.getBoardELRP12_1 shouldBe board.formatBoardRow(tokens0.take(mode.cntTokens))
      updatedBoard.getBoardELRP12_2 shouldBe board.formatBoardRow(tokens0.drop(mode.cntTokens))
      updatedBoard.getBoardELRP34_1 shouldBe board.formatBoardRow(tokens1.take(mode.cntTokens))
      updatedBoard.getBoardELRP34_2 shouldBe board.formatBoardRow(tokens1.drop(mode.cntTokens))
    }

    "render the PlayingField as a string" in {
      val pf = mode.createPlayingField(players)
      val str = mode.renderPlayingField(pf)
      str should include ("Alice")
      str should include ("Bob")
      str should include ("Charlie")
    }

    "renderPlayingField returns error string if None" in {
      mode.renderPlayingField(None) should include ("No playing field available")
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
      val stack = new TokenStack(List())
      val manyTokens = stack.drawMultipleTokens(30)
      val player = Player("Azra", manyTokens._1)
      val board = new Board(24, 14, 3, 1, "down")

      val updatedOpt = mode.updateBoardSinglePlayer(player, board)
      updatedOpt.isDefined shouldBe true

      val updatedBoard = updatedOpt.get
      updatedBoard.getBoardELRP12_1 shouldEqual board.formatBoardRow(manyTokens._1.take(mode.cntTokens))
      updatedBoard.getBoardELRP12_2 shouldEqual board.formatBoardRow(manyTokens._1.drop(mode.cntTokens))
      updatedBoard.getBoardEUD shouldEqual board.createBoardFrameSingle(manyTokens._1.take(mode.cntTokens))
    }
  }
}