package de.htwg.se.rummikub.util.commands

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.state.GameState

class AppendTokenCommandSpec extends AnyWordSpec {
  "An AppendTokenCommand" should {
    val player = Player("Anna", tokens = List(NumToken(1, Color.RED)))
    val innerField = new Table(1, 1, List(List()))
    val pf = PlayingField(
      innerField = innerField,
      players = List(player),
      boards = List(),
      stack = TokenStack(List())
    )
    val controller = new Controller(GameModeFactory.createGameMode(2, List("Anna", "Ben")).get)
    controller.playingField = Some(pf)
    controller.setStateInternal(GameState(innerField, Vector(player), Vector(), 0, TokenStack(List())))
    val token = NumToken(1, Color.RED)
    val cmd = new AppendTokenCommand(controller, token, 0, isRow = true, player)

    "append a token to the table on doStep" in {
      cmd.doStep()
      controller.playingField.get.innerField.tokensOnTable(0) should contain (token)
      controller.getState.players.head.tokens should not contain (token)
    }

    "restore the old state on undoStep" in {
      cmd.doStep()
      cmd.undoStep()
      controller.getState.players.head.tokens should contain (token)
    }

    "redo the step after undo" in {
      cmd.doStep()
      cmd.undoStep()
      cmd.redoStep()
      controller.playingField.get.innerField.tokensOnTable(0) should contain (token)
      controller.getState.players.head.tokens should not contain (token)
    }

    "print message if no state is available on doStep" in {
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Anna", "Ben")).get)
      controller.playingField = None 
      val token = NumToken(1, Color.RED)
      val player = Player("Anna", tokens = List(token))
      val cmd = new AppendTokenCommand(controller, token, 0, isRow = true, player)
      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(out) {
        cmd.doStep()
      }
      out.toString should include ("No state available.")
    }

    "print message if no state is available on undoStep" in {
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Anna", "Ben")).get)
      val token = NumToken(1, Color.RED)
      val player = Player("Anna", tokens = List(token))
      val cmd = new AppendTokenCommand(controller, token, 0, isRow = true, player)
      cmd.oldState = None
      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(out) {
        cmd.undoStep()
      }
      out.toString should include ("No state available.")
    }

    "not modify other rows when appending a token" in {
      val player = Player("Anna", tokens = List(NumToken(1, Color.RED)))
      val otherToken = NumToken(2, Color.BLUE)
      val innerField = new Table(2, 1, List(List(), List(otherToken)))
      val pf = PlayingField(
        innerField = innerField,
        players = List(player),
        boards = List(),
        stack = TokenStack(List())
      )
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Anna", "Ben")).get)
      controller.playingField = Some(pf)
      controller.setStateInternal(GameState(innerField, Vector(player), Vector(), 0, TokenStack(List())))
      val token = NumToken(1, Color.RED)
      val cmd = new AppendTokenCommand(controller, token, 0, isRow = true, player)

      cmd.doStep()
      controller.playingField.get.innerField.tokensOnTable(1) should contain only otherToken
    }
  }
}

