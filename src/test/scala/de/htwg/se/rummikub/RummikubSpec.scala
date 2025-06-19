package de.htwg.se.rummikub

import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.PlayingField
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.rummikub.aview.Tui

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface

import de.htwg.se.rummikub.RummikubDependencyModule.given

class RummikubSpec extends AnyWordSpec with Matchers {
  "Rummikub" should {
    "have a main method that runs without exceptions" in {
      noException should be thrownBy {
        val input = new ByteArrayInputStream("quit\n".getBytes)
        Console.withIn(input) {
          Rummikub.main(Array())
        }
      }
    }

    "initialize the game correctly with default players" in {
      controller.setupNewGame(2, List("Emilia", "Noah"))

      controller.getPlayingField.get.getPlayers should have size 2
      controller.getPlayingField.get.getPlayers.head.getName should be("Emilia")
      controller.getPlayingField.get.getPlayers(1).getName should be("Noah")
    }

    "process commands through the Tui" in {
      val input = new ByteArrayInputStream("help\nquit\n".getBytes)
      val output = new ByteArrayOutputStream()
      Console.withIn(input) {
        Console.withOut(new PrintStream(output)) {
          Rummikub.main(Array())
        }
      }
      output.toString should include("Welcome to")
      output.toString should include("Please enter a command:")
      output.toString should include("help")
    }

    "handle invalid input gracefully" in {
      val input = new ByteArrayInputStream("invalid\nquit\n".getBytes)
      val output = new ByteArrayOutputStream()
      Console.withIn(input) {
        Console.withOut(new PrintStream(output)) {
          Rummikub.main(Array())
        }
      }
      output.toString should include("Please enter a command:")
    }
  }
}