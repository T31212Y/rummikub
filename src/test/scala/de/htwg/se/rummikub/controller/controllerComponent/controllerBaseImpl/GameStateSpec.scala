package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Table
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Board
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.TokenStack
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.Color

class GameStateSpec extends AnyWordSpec {
  "A GameState" should {
    val table = Table(2, 14)
    val player1 = Player("A")
    val player2 = Player("B")
    val players = Vector(player1, player2)
    val board1 = Board(24, 14, 2, 2, "default", 10)
    val board2 = Board(24, 14, 2, 2, "default", 10)
    val boards = Vector(board1, board2)
    val stack = TokenStack(List.empty)
    val state = GameState(table, players, boards, 0, stack)

    "return the current player" in {
      state.currentPlayer shouldBe player1
    }

    "return the current board" in {
      state.currentBoard shouldBe board1
    }

    "update the current player" in {
      val updated = player1.copy(tokens = List(NumToken(1, Color.RED)))
      val newState = state.updateCurrentPlayer(updated)
      newState.players(0) shouldBe updated
      newState.players(1) shouldBe player2
    }

    "update the current board" in {
      val updated = board1.copy(dest = "changed")
      val newState = state.updateCurrentBoard(updated)
      newState.boards(0) shouldBe updated
      newState.boards(1) shouldBe board2
    }

    "update a player by id" in {
      val updated = player2.copy(tokens = List(NumToken(2, Color.BLUE)))
      val newState = state.updatePlayerById("B", updated)
      newState.players(1) shouldBe updated
      newState.players(0) shouldBe player1
    }

    "update a board by index" in {
      val updated = board2.copy(dest = "other")
      val newState = state.updateBoardByIndex(1, updated)
      newState.boards(1) shouldBe updated
      newState.boards(0) shouldBe board1
    }

    "update the table" in {
      val newTable = Table(2, 14, List(List(NumToken(1, Color.RED))))
      val newState = state.updateTable(newTable)
      newState.table shouldBe newTable
    }

    "advance to the next turn" in {
      val newState = state.nextTurn
      newState.currentPlayerIndex shouldBe 1
      newState.currentPlayer shouldBe player2
      newState.nextTurn.currentPlayerIndex shouldBe 0
    }
  }
}