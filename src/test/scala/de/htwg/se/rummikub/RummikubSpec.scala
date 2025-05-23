package de.htwg.se.rummikub

import de.htwg.se.rummikub.model.{Player, PlayingField, GameModeFactory}
import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.aview.Tui

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

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
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      controller.setupNewGame(2, List("Emilia", "Noah"))

      controller.playingField.get.players should have size 2
      controller.playingField.get.players.head.name should be("Emilia")
      controller.playingField.get.players(1).name should be("Noah")
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