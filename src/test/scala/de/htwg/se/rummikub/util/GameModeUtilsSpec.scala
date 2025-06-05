package de.htwg.se.rummikub.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.Color
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Board

class GameModeUtilsSpec extends AnyWordSpec {
  "GameModeUtils" should {
    "update board for single player with <= cntTokens" in {
      val player = Player("A", List.fill(10)(NumToken(1, Color.RED)))
      val board = Board(24, 14, 2, 2, "default", 10)
      val updated = GameModeUtils.updateBoardSinglePlayer(player, board)
      updated shouldBe defined
      updated.get.getBoardELRP12_1 should not be empty
    }

    "update board for single player with > cntTokens" in {
      val player = Player("A", List.fill(30)(NumToken(1, Color.RED)))
      val board = Board(24, 14, 2, 2, "default", 10)
      val updated = GameModeUtils.updateBoardSinglePlayer(player, board)
      updated shouldBe defined
      updated.get.getBoardELRP12_1 should not be empty
      updated.get.getBoardELRP12_2 should not be empty
    }

    "update board for two players with <= cntTokens" in {
      val p1 = Player("A", List.fill(10)(NumToken(1, Color.RED)))
      val p2 = Player("B", List.fill(10)(NumToken(2, Color.BLUE)))
      val board = Board(24, 14, 2, 2, "default", 10)
      val updated = GameModeUtils.updateBoardMultiPlayer(List(p1, p2), board)
      updated shouldBe defined
      updated.get.getBoardELRP12_1 should not be empty
      updated.get.getBoardELRP34_1 should not be empty
    }

    "update board for two players with > cntTokens" in {
      val p1 = Player("A", List.fill(30)(NumToken(1, Color.RED)))
      val p2 = Player("B", List.fill(30)(NumToken(2, Color.BLUE)))
      val board = Board(24, 14, 2, 2, "default", 10)
      val updated = GameModeUtils.updateBoardMultiPlayer(List(p1, p2), board)
      updated shouldBe defined
      updated.get.getBoardELRP12_2 should not be empty
      updated.get.getBoardELRP34_2 should not be empty
    }

    "create a line with player name" in {
      val line = GameModeUtils.lineWithPlayerName("*", 20, "Anna")
      line shouldBe defined
      line.get should include ("Anna")
      line.get.length should be <= 20
    }

    "create a line with two player names" in {
      val line = GameModeUtils.lineWith2PlayerNames("*", 30, "Anna", "Ben")
      line shouldBe defined
      line.get should include ("Anna")
      line.get should include ("Ben")
      line.get.length should be <= 30
    }

    "clean render output" in {
      GameModeUtils.cleanRenderOutput("xxyy") shouldBe "    "
      GameModeUtils.cleanRenderOutput("abc") shouldBe "abc"
    }
  }
}
