package de.htwg.se.rummikub.util.commands

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.state.GameState

class AddRowCommandSpec extends AnyWordSpec {

  "An AddRowCommand" should {
    "remove tokens from player and update removedTokens on doStep" in {
        val player = Player("TestPlayer", tokens = List(NumToken(1, Color.RED), NumToken(2, Color.BLUE)))
        val row = Row(List("1:red", "2:blue"))
        val stack = TokenStack()
        val controller = new Controller(GameModeFactory.createGameMode(2, List("TestPlayer", "Other")).get)
        controller.setupNewGame(2, List("TestPlayer", "Other"))

        val testToken = NumToken(1, Color.RED)
        val testToken2 = NumToken(2, Color.BLUE)
        val updatedPlayer = player.copy(tokens = List())

        val cmd = new AddRowCommand(controller, row, player, stack)

        cmd.doStep()

        cmd.removedTokens should contain allOf (testToken, testToken2)
    }

    "restore old state on undoStep" in {
        val player = Player("TestPlayer", tokens = List(NumToken(1, Color.RED)))
        val row = Row(List("1:red"))
        val stack = TokenStack()
        val controller = new Controller(GameModeFactory.createGameMode(2, List("TestPlayer", "Other")).get)
        controller.setupNewGame(2, List("TestPlayer", "Other"))

        val initialState = controller.getState
        val cmd = new AddRowCommand(controller, row, player, stack)

        cmd.doStep()
        cmd.undoStep()

        controller.getState shouldEqual initialState
    }

    "print message if no old state on undoStep" in {
        val player = Player("TestPlayer")
        val row = Row(List("1:red"))
        val stack = TokenStack()
        val controller = new Controller(GameModeFactory.createGameMode(2, List("TestPlayer", "Other")).get)
        controller.setupNewGame(2, List("TestPlayer", "Other"))

        val cmd = new AddRowCommand(controller, row, player, stack) {
            oldState = None
        }

        val stream = new java.io.ByteArrayOutputStream()
        Console.withOut(stream) {
            cmd.undoStep()
        }
        stream.toString should include ("No State available.")
    }

    "redoStep should call doStep" in {
        val player = Player("TestPlayer")
        val row = Row(List("1:red"))
        val stack = TokenStack()
        val controller = new Controller(GameModeFactory.createGameMode(2, List("TestPlayer", "Other")).get)
        controller.setupNewGame(2, List("TestPlayer", "Other"))

        var doStepCalled = false

        val cmd = new AddRowCommand(controller, row, player, stack) {
            override def doStep(): Unit = doStepCalled = true
        }
        cmd.redoStep()
        doStepCalled shouldBe true
    }
  }
}