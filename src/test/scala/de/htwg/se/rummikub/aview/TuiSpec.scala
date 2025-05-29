package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.rummikub.model._

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

class TuiSpec extends AnyWordSpec with Matchers {

  "A Tui" should {

    val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
    val tui = new Tui(controller)

    "show welcome message" in {
      val welcome = tui.showWelcome
      welcome should not be empty
      welcome.exists(_.contains("Welcome to")) should be (true)
    }

    "show help text" in {
      tui.showHelp should include ("Type 'help' for a list of commands.")
    }

    "show help page" in {
      val helpPage = tui.showHelpPage
      helpPage should not be empty
      helpPage should be (Vector("Available commands:",
               "new - Create a new game",
               "start - Start the game",
               "quit - Exit the game"
              ))
    }

    "ask for number of players" in {
      tui.askAmountOfPlayers should be ("Please enter the number of players (2-4):")
    }

    "ask for player names" in {
      tui.askPlayerNames should be ("Please enter the names of the players (comma-separated):")
    }

    "show goodbye message" in {
      tui.inputCommands("quit")
      controller.gameEnded = true
      tui.showGoodbye should include ("Thank you for playing Rummikub! Goodbye!")
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
      tui.inputCommands("quit")
    }

    "handle 'help' command input" in {
      val outContent = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outContent)) {
        tui.inputCommands("help")
      }

      val output = outContent.toString
      output should include("Available commands:")
      output should include("new - Create a new game")
      tui.inputCommands("quit")
    }

    "handle 'start' command input" in {
      val in = new ByteArrayInputStream("end\n".getBytes)
      Console.withIn(in) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.inputCommands("start")
        }
        val output = out.toString
        output should (include("It's") or include("Player") or include("turn"))
      }
      tui.inputCommands("quit")
    }

    "handle 'quit' command input" in {
      val outContent = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outContent)) {
        tui.inputCommands("quit")
      }
      outContent.toString should include ("Thank you for playing Rummikub! Goodbye!")
    }

    "ignore unknown command input" in {
      val outContent = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(outContent)) {
        tui.inputCommands("foobar")
      }
      outContent.toString should not include ("Exception")
    }

    "print invalid command for unknown input" in {
      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) {
        tui.processGameInput("foobar")
      }
      out.toString should include ("Invalid command.")
    }

    "handle 'draw' command" in {
      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) {
        tui.processGameInput("draw")
      }
      out.toString should include ("Drawing a token")
    }

    "handle 'pass' command" in {
      val player1 = Player("Emilia", tokens = List(NumToken(1, Color.RED)), commandHistory = List("row:10:red,10:blue,10:green"), firstMoveTokens = List(NumToken(11, Color.RED), NumToken(12, Color.BLUE), NumToken(13, Color.GREEN)), hasCompletedFirstMove = true)
      val player2 = Player("Noah", tokens = List(NumToken(2, Color.BLUE)))
      val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
      val tui = new Tui(controller)
      
      controller.setupNewGame(2, List("Emilia", "Noah"))
      controller.playingField = Some(controller.playingField.get.copy(players = List(player1, player2)))
      
      val stack = TokenStack()

      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) {
        tui.processGameInput("pass")
      }
      println("output test" + out.toString)
      out.toString should include ("ended their turn")
    }

    "handle 'undo' and 'redo' commands" in {
      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) {
        tui.processGameInput("undo")
        tui.processGameInput("redo")
      }
      out.toString should include ("Undo successful.")
    }

    "handle 'end' command" in {
      controller.gameEnded = false
      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) {
        tui.processGameInput("end")
      }
      out.toString should include ("Exiting the game")
      controller.gameEnded shouldBe true
    }

    "handle 'group' command" in {
      val in = new ByteArrayInputStream("1:red,2:blue\n".getBytes)
      Console.withIn(in) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("group")
        }
        out.toString should (include ("Enter the tokens to play as group") or include ("Group"))
      }
    }

    "handle 'row' command" in {
      val in = new ByteArrayInputStream("1:red,2:blue\n".getBytes)
      Console.withIn(in) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("row")
        }
        out.toString should (include ("Enter the tokens to play as row") or include ("Row"))
      }
    }

    "handle 'appendToRow' command" in {
      val in = new ByteArrayInputStream("1:red\n0\n".getBytes)
      Console.withIn(in) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("appendToRow")
        }
        out.toString should include ("Enter the token to append")
      }
    }

    "handle 'appendToGroup' command" in {
      val in = new ByteArrayInputStream("1:red\n0\n".getBytes)
      Console.withIn(in) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("appendToGroup")
        }
        out.toString should include ("Enter the token to append")
      }
    }
  }
}