package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.Color
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.TokenStack
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Table
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface

class AppendTokenCommandSpec extends AnyWordSpec {
  val gameModeFactory = new GameModeFactory
  val controller: Controller = new Controller(gameModeFactory.createGameMode(2, List("Emilia", "Noah")).get, gameModeFactory)
  given ControllerInterface = controller

  "An AppendTokenCommand" should {
    val player = Player("Anna", tokens = List(NumToken(1, Color.RED)))
    val innerField = new Table(1, 1, List(List()))
    val pf = PlayingField(
      innerField = innerField,
      players = List(player),
      boards = List(),
      stack = TokenStack(List())
    )

    controller.playingField = Some(pf)
    controller.setStateInternal(GameState(innerField, Vector(player), Vector(), 0, TokenStack(List())))
    val token = NumToken(1, Color.RED)
    val cmd = new AppendTokenCommand(controller, token, 0, isRow = true, player)

    "append a token to the table on doStep" in {
      cmd.doStep()
      controller.playingField.get.getInnerField.getTokensOnTable(0) should contain (token)
      controller.getState.getPlayers.head.getTokens should not contain (token)
    }

    "restore the old state on undoStep" in {
      cmd.doStep()
      cmd.undoStep()
      controller.getState.getPlayers.head.getTokens should contain (token)
    }

    "redo the step after undo" in {
      cmd.doStep()
      cmd.undoStep()
      cmd.redoStep()
      controller.playingField.get.getInnerField.getTokensOnTable(0) should contain (token)
      controller.getState.getPlayers.head.getTokens should not contain (token)
    }

    "print message if no state is available on doStep" in {
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
      controller.playingField = Some(pf)
      controller.setStateInternal(GameState(innerField, Vector(player), Vector(), 0, TokenStack(List())))
      val token = NumToken(1, Color.RED)
      val cmd = new AppendTokenCommand(controller, token, 0, isRow = true, player)

      cmd.doStep()
      controller.playingField.get.getInnerField.getTokensOnTable(1) should contain only otherToken
    }
  }
}

