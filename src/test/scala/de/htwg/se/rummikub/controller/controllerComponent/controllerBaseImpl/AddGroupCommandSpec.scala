package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.Color
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.TokenStack
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.Group
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory
import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface

class AddGroupCommandSpec extends AnyWordSpec {
  val gameModeFactory = new GameModeFactory
  val controller: Controller = new Controller(gameModeFactory.createGameMode(2, List("Emilia", "Noah")).get, gameModeFactory)
  given ControllerInterface = controller

  "An AddGroupCommand" should {
    "remove tokens from player and update removedTokens on doStep" in {
      val player = Player("Emilia", tokens = List(NumToken(1, Color.RED), NumToken(1, Color.BLUE)))
      val group = Group(List(NumToken(1, Color.RED), NumToken(1, Color.BLUE)))
      val stack = TokenStack()
      controller.setupNewGame(2, List("Emilia", "Noah"))

      val testToken = NumToken(1, Color.RED)
      val testToken2 = NumToken(1, Color.BLUE)

      val cmd = new AddGroupCommand(controller, group, player, stack)

      cmd.doStep()

      cmd.removedTokens should contain allOf (testToken, testToken2)
    }

    "restore old state on undoStep" in {
      val player = Player("Emilia", tokens = List(NumToken(1, Color.RED)))
      val group = Group(List(NumToken(1, Color.RED)))
      val stack = TokenStack()
      controller.setupNewGame(2, List("Emilia", "Noah"))

      val initialState = controller.getState
      val cmd = new AddGroupCommand(controller, group, player, stack)

      cmd.doStep()
      cmd.undoStep()

      controller.getState shouldEqual initialState
    }

    "print message if no old state on undoStep" in {
      val player = Player("Emilia")
      val group = Group(List(NumToken(1, Color.RED)))
      val stack = TokenStack()
      controller.setupNewGame(2, List("Emilia", "Noah"))

      val cmd = new AddGroupCommand(controller, group, player, stack) {
        oldState = None
      }

      val stream = new java.io.ByteArrayOutputStream()
      Console.withOut(stream) {
        cmd.undoStep()
      }
      stream.toString should include ("No State available.")
    }

    "redoStep should call doStep" in {
      val player = Player("Emilia")
      val group = Group(List(NumToken(1, Color.RED)))
      val stack = TokenStack()
      controller.setupNewGame(2, List("Emilia", "Noah"))

      var doStepCalled = false

      val cmd = new AddGroupCommand(controller, group, player, stack) {
        override def doStep(): Unit = doStepCalled = true
      }
      cmd.redoStep()
      doStepCalled shouldBe true
    }
  }
}
