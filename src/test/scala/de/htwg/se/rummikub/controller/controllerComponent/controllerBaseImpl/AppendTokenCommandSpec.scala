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

import com.google.inject.Guice
import de.htwg.se.rummikub.RummikubModule

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface

class AppendTokenCommandSpec extends AnyWordSpec {
  "An AppendTokenCommand" should {
    val injector = Guice.createInjector(new RummikubModule)
    val controller = injector.getInstance(classOf[ControllerInterface])
    controller.setupNewGame(2, List("Emilia", "Noah"))
    
    val tokenStackFactory = injector.getInstance(classOf[TokenStackFactoryInterface])
    val tokenStructureFactory = injector.getInstance(classOf[TokenStructureFactoryInterface])

    val player = Player("Anna", tokens = List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
    val innerField = new Table(1, 1, List(List()))
    val pf = PlayingField(
      innerField = innerField,
      players = List(player),
      boards = List(),
      stack = TokenStack(List())
    )

    val token = NumToken(1, Color.RED)

    controller.setPlayingField(Some(pf))
    controller.setStateInternal(GameState(innerField, Vector(player), Vector(), 0, TokenStack(List())))

    val cmd = new AppendTokenCommand(controller, token, 0, 0, isRow = true, player) // insertAt = 0

    "append a token to the table on doStep" in {
      cmd.doStep()
      controller.getPlayingField.get.getInnerField.getTokensOnTable(0) should contain (token)
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
      controller.getPlayingField.get.getInnerField.getTokensOnTable(0) should contain (token)
      controller.getState.getPlayers.head.getTokens should not contain (token)
    }

    "print message if no state is available on doStep" in {
      controller.setPlayingField(None)
      val cmd = new AppendTokenCommand(controller, token, 0, 0, isRow = true, player)
      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(out) {
        cmd.doStep()
      }
      out.toString should include ("No state available.")
    }

    "print message if no state is available on undoStep" in {
      val cmd = new AppendTokenCommand(controller, token, 0, 0, isRow = true, player)
      cmd.oldState = None
      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(out) {
        cmd.undoStep()
      }
      out.toString should include ("No state available.")
    }

    "not modify other rows when appending a token" in {
      val otherToken = NumToken(2, Color.BLUE)
      val innerField = new Table(2, 1, List(List(), List(otherToken)))

      controller.setStateInternal(GameState(innerField, Vector(player), Vector(), 0, TokenStack(List())))

      val cmd = new AppendTokenCommand(controller, token, 0, 0, isRow = true, player)

      cmd.doStep()
      controller.getPlayingField.get.getInnerField.getTokensOnTable(1) should contain only otherToken
    }
  }
}