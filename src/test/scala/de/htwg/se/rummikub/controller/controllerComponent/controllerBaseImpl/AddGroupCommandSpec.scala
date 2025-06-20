package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.GameState
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.Color
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.TokenStack
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.Group
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory
import de.htwg.se.rummikub.aview.Tui

import com.google.inject.Guice
import de.htwg.se.rummikub.RummikubModule

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface

class AddGroupCommandSpec extends AnyWordSpec {

  "An AddGroupCommand" should {
    val injector = Guice.createInjector(new RummikubModule)
    val controller = injector.getInstance(classOf[ControllerInterface])
    controller.setupNewGame(2, List("Emilia", "Noah"))

    val tokenStackFactory = injector.getInstance(classOf[TokenStackFactoryInterface])
    val tokenStructureFactory = injector.getInstance(classOf[TokenStructureFactoryInterface])

    "remove tokens from player and update removedTokens on doStep" in {
        val player = Player("Emilia", tokens = List(NumToken(1, Color.RED), NumToken(1, Color.BLUE)), tokenStructureFactory = tokenStructureFactory)
        val group = Group(List(NumToken(1, Color.RED), NumToken(1, Color.BLUE)))
        val stack = tokenStackFactory.createShuffledStack

        val testToken = NumToken(1, Color.RED)
        val testToken2 = NumToken(1, Color.BLUE)
        val updatedPlayer = player.copy(tokens = List())

        val cmd = new AddGroupCommand(controller, group, player, stack)

        cmd.doStep()

        cmd.removedTokens should contain allOf (testToken, testToken2)
    }

    "restore old state on undoStep" in {
        val player = Player("Emilia", tokens = List(NumToken(1, Color.RED)), tokenStructureFactory = tokenStructureFactory)
        val group = Group(List(NumToken(1, Color.RED)))
        val stack = tokenStackFactory.createShuffledStack

        val initialState = controller.getState
        val cmd = new AddGroupCommand(controller, group, player, stack)

        cmd.doStep()
        cmd.undoStep()

        controller.getState shouldEqual initialState
    }

    "print message if no old state on undoStep" in {
        val player = Player("Emilia", tokenStructureFactory = tokenStructureFactory)
        val group = Group(List(NumToken(1, Color.RED)))
        val stack = tokenStackFactory.createShuffledStack

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
        val player = Player("Emilia", tokenStructureFactory = tokenStructureFactory)
        val group = Group(List(NumToken(1, Color.RED)))
        val stack = tokenStackFactory.createShuffledStack

        var doStepCalled = false

        val cmd = new AddGroupCommand(controller, group, player, stack) {
            override def doStep(): Unit = doStepCalled = true
        }
        cmd.redoStep()
        doStepCalled shouldBe true
    }
  }
}