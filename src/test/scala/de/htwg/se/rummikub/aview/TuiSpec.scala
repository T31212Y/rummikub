package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.rummikub.model.{Color, Player, PlayingField, Token}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

class TuiSpec extends AnyWordSpec with Matchers {

  "A Tui" should {

    val controller = new Controller(new PlayingField(2, List(new Player("Emilia"), new Player("Noah"))))
    val tui = new Tui(controller)

    "show welcome message" in {
      val welcome = tui.showWelcome()
      welcome should not be empty
      welcome.head should be ("Welcome to")
    }

    "show help text" in {
      tui.showHelp() should be ("Type 'help' for a list of commands.")
    }

    "show help page" in {
      val helpPage = tui.showHelpPage()
      helpPage should not be empty
      helpPage should be (Vector("Available commands:",
               "new - Create a new game",
               "start - Start the game",
               "quit - Exit the game"
              ))
    }

    "ask for number of players" in {
      tui.askAmountOfPlayers() should be ("Please enter the number of players (2-4):")
    }

    "ask for player names" in {
      tui.askPlayerNames() should include ("Please enter the names of the players (comma-separated):")
    }

    "show goodbye message" in {
      tui.showGoodbye() should include ("Thank you for playing Rummikub! Goodbye!")
    }
    "handle 'new' command" in {
      val in = new ByteArrayInputStream("2\nAlice,Bob\n".getBytes)
      Console.withIn(in) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.inputCommands("new")
        }
        val output = out.toString
        output should include("Creating a new game")
        output should include("Please enter the number of players")
        output should include("Please enter the names of the players")
      }
    }


    "handle 'help' command input" in {
      val outContent = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outContent)) {
        tui.inputCommands("help")
      }

      val output = outContent.toString
      output should include("Available commands:")
      output should include("new - Create a new game")
    }

    "handle 'start' command input" in {
      val outContent = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outContent)) {
        tui.inputCommands("start")
      }

      val output = outContent.toString
      output should (include("It's") or include("Player") or include("turn")) // abhängig vom Output deiner playGame()-Methode
    }

    "handle unknown command input" in {
      val outContent = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outContent)) {
        tui.inputCommands("exit")
      }

      val output = outContent.toString
      output shouldBe("")
    }
    "handle 'quit' command" in {
      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) {
        tui.inputCommands("quit")
      }
      val output = out.toString.trim
      output should include("Thank you for playing Rummikub! Goodbye!")
    }
  }
}



    /*"create a new game on 'new' command" in {
      val simulatedInput = "2\nAnki,Imma\n" 
      Console.withIn(new ByteArrayInputStream(simulatedInput.getBytes())) {
        val result = tui.inputCommands("new")
        result.amountOfPlayers shouldBe 2
        result.players.map(_.name) should contain allOf ("Anki", "Imma")
      }
    }

    "start a game on 'start' command with simulated input" in {
      val simulatedInput = "end\n"
      Console.withIn(new ByteArrayInputStream(simulatedInput.getBytes())) {
        captureOutput {
          val result = tui.inputCommands("start")
          result.players.map(_.name) should contain allElementsOf List("Anki", "Imma")
        } should include ("Starting the game")
      }
    }

  }
  def captureOutput(testCode: => Any): String = {
    val outCapture = new ByteArrayOutputStream()
    Console.withOut(new PrintStream(outCapture)) {
      testCode
    }
    outCapture.toString
  }
} */
