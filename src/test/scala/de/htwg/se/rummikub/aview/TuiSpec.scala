package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.Controller
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.Color
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.TokenStack
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player

import com.google.inject.Guice
import de.htwg.se.rummikub.RummikubModule

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface
import de.htwg.se.rummikub.controller.controllerComponent.GameStateInterface

import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._

class TuiSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "A Tui" should {
    val injector = Guice.createInjector(new RummikubModule)
    val controller = injector.getInstance(classOf[ControllerInterface])
    controller.setupNewGame(2, List("Emilia", "Noah"))

    val tui = new Tui(controller)
    val tokenStackFactory = injector.getInstance(classOf[TokenStackFactoryInterface])
    val tokenStructureFactory = injector.getInstance(classOf[TokenStructureFactoryInterface])

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
      controller.setGameEnded(true)
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
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))

      val tui = new Tui(controller)
      val tokenStackFactory = injector.getInstance(classOf[TokenStackFactoryInterface])
      val tokenStructureFactory = injector.getInstance(classOf[TokenStructureFactoryInterface])

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
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))

      val tui = new Tui(controller)
      val tokenStackFactory = injector.getInstance(classOf[TokenStackFactoryInterface])
      val tokenStructureFactory = injector.getInstance(classOf[TokenStructureFactoryInterface])

      controller.setGameEnded(true)

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
      val player1 = Player("Emilia", tokens = List(NumToken(1, Color.RED)), commandHistory = List("group:10:red,10:blue,10:green"), firstMoveTokens = List(NumToken(11, Color.RED), NumToken(12, Color.RED), NumToken(13, Color.RED)), hasCompletedFirstMove = true, tokenStructureFactory = tokenStructureFactory)
      val player2 = Player("Noah", tokens = List(NumToken(2, Color.BLUE)), commandHistory = List("row:11:blue,12:blue,13:blue"), firstMoveTokens = List(NumToken(11, Color.BLUE), NumToken(12, Color.BLUE), NumToken(13, Color.BLUE)), tokenStructureFactory = tokenStructureFactory)

      val tui = new Tui(controller)

      controller.setPlayingField(Some(controller.getPlayingField.get.updated(newPlayers = List(player1, player2), newBoards = controller.getPlayingField.get.getBoards, newInnerField = controller.getPlayingField.get.getInnerField)))
      
      val stack = tokenStackFactory.createShuffledStack

      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) {
        tui.processGameInput("pass")
      }
      println("output test: " + out.toString + ", currentPlayer: " + controller.getState.currentPlayer + ", commandHistory: " + controller.getState.currentPlayer.getCommandHistory)
      out.toString should include ("ended their turn")
    }

    "print warning if pass is called before first move" in {
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))
      val tui = new Tui(controller)

      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) {
        tui.processGameInput("pass")
      }
      out.toString should include ("You must make a move before passing your turn.")
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
      controller.setGameEnded(false)
      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) {
        tui.processGameInput("end")
      }
      out.toString should include ("Exiting the game")
      controller.getGameEnded shouldBe true
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
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))

      val tui = new Tui(controller)
      val tokenStackFactory = injector.getInstance(classOf[TokenStackFactoryInterface])
      val tokenStructureFactory = injector.getInstance(classOf[TokenStructureFactoryInterface])

      val in = new ByteArrayInputStream("1:red\n0\n0\n".getBytes)
      Console.withIn(in) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("appendToRow")
        }
        out.toString should include ("Enter the token to append")
      }
    }

    "handle 'appendToGroup' command" in {
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))

      val tui = new Tui(controller)
      val tokenStackFactory = injector.getInstance(classOf[TokenStackFactoryInterface])
      val tokenStructureFactory = injector.getInstance(classOf[TokenStructureFactoryInterface])

      val in = new ByteArrayInputStream("1:red\n0\n0\n".getBytes)
      Console.withIn(in) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("appendToGroup")
        }
        out.toString should include ("Enter the token to append")
      }
    }

    "display the available commands" in {
      val result = tui.showAvailableCommands

      val expected = Vector(
        "Available commands:",
        "draw - Draw a token from the stack and pass your turn",
        "pass - Pass your turn",
        "row - Play a row of tokens",
        "group - Play a group of tokens",
        "appendToRow - Append a token to an existing row",
        "appendToGroup - Append a token to an existing group",
        "undo - Undo last move",
        "redo - Redo last undone move"
      )

      result shouldBe expected
    }

    "showTableTokensWithIndex should print tokens with indices" in {
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))
      val tui = new Tui(controller)

      val token1 = NumToken(1, Color.RED)
      val token2 = NumToken(2, Color.BLUE)
      val table = controller.getState.getTable

      val newTable = table.add(List(token1, token2))
      val newState = controller.getState.updateTable(newTable)
      controller.setStateInternal(newState)

      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) {
        tui.showTableTokensWithIndex()
      }
      val output = out.toString
      output should include ("Tokens on Table with Index:")
      output should include ("[0] \u001b[31m 1\u001b[0m")
      output should include ("[1] \u001b[34m 2\u001b[0m")
    }

    "handle 'store' command" in {
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))
      val tui = new Tui(controller)

      val token1 = NumToken(1, Color.RED)
      val token2 = NumToken(2, Color.BLUE)
      val table = controller.getState.getTable
      val newTable = table.add(List(token1, token2))
      val newState = controller.getState.updateTable(newTable)
      controller.setStateInternal(newState)

      val in = new ByteArrayInputStream("0\n".getBytes)
      Console.withIn(in) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("store")
        }
        val output = out.toString
        output should include ("Tokens on Table with Index:")
        output should include ("Enter the index of the token to store:")
        output should (include ("Token stored successfully.") or include ("Invalid token index!"))
      }
    }

    "handle 'restore' command" in {
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))
      val tui = new Tui(controller)

      val token1 = NumToken(1, Color.RED)
      val table = controller.getState.getTable
      val newTable = table.add(List(token1))
      val newState = controller.getState.updateTable(newTable)
      controller.setStateInternal(newState)

      val inStore = new ByteArrayInputStream("0\n".getBytes)
      Console.withIn(inStore) {
        tui.processGameInput("store")
      }
      val inRestore = new ByteArrayInputStream("1:red\n0\n0\n".getBytes)
      Console.withIn(inRestore) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("restore")
        }
        val output = out.toString
        output should include ("Tokens im Storage:")
        output should include ("Enter the token string")
        output should include ("Enter the group index:")
        output should include ("Enter the insert position in the group:")
        output should (
          include ("moved from Storage") or
          include ("Invalid input!") or
          include ("not found") or
          include ("invalid group-index!")
        )
      }
    }

    "handle 'restore' command with empty storage" in {
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))
      val tui = new Tui(controller)

      val inRestore = new ByteArrayInputStream("1:red\n0\n0\n".getBytes)
      Console.withIn(inRestore) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("restore")
        }
        val output = out.toString
        output should include ("Tokens im Storage:")
        output should include ("(keine Tokens im Storage)")
      }
    }

    "handle 'restore' command with invalid group index input" in {
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))
      val tui = new Tui(controller)

      val token1 = NumToken(1, Color.RED)
      val table = controller.getState.getTable
      val newTable = table.add(List(token1))
      val newState = controller.getState.updateTable(newTable)
      controller.setStateInternal(newState)

      val inStore = new ByteArrayInputStream("0\n".getBytes)
      Console.withIn(inStore) {
        tui.processGameInput("store")
      }

      val inRestore = new ByteArrayInputStream("1:red\nfoo\nbar\n".getBytes)
      Console.withIn(inRestore) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("restore")
        }
        val output = out.toString
        output should include ("Invalid input! Group index and insert position must be integers.")
      }
    }

    "handle 'store' command with invalid token index" in {
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))
      val tui = new Tui(controller)

      val token1 = NumToken(1, Color.RED)
      val table = controller.getState.getTable
      val newTable = table.add(List(token1))
      val newState = controller.getState.updateTable(newTable)
      controller.setStateInternal(newState)

      val in = new ByteArrayInputStream("5\n".getBytes)
      Console.withIn(in) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("store")
        }
        val output = out.toString
        output should include ("Invalid token index!")
      }
    }

    "handle 'store' command with non-numeric input" in {
      val injector = Guice.createInjector(new RummikubModule)
      val controller = injector.getInstance(classOf[ControllerInterface])
      controller.setupNewGame(2, List("Emilia", "Noah"))
      val tui = new Tui(controller)

      val token1 = NumToken(1, Color.RED)
      val table = controller.getState.getTable
      val newTable = table.add(List(token1))
      val newState = controller.getState.updateTable(newTable)
      controller.setStateInternal(newState)

      val in = new ByteArrayInputStream("foo\n".getBytes)
      Console.withIn(in) {
        val out = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(out)) {
          tui.processGameInput("store")
        }
        val output = out.toString
        output should include ("Invalid input! Please enter a number.")
      }
    }

    "print error message if it is not possible to set new state in 'pass' command" in {
      val controller = mock[ControllerInterface]
      
      val oldState = mock[GameStateInterface]
      val newState = mock[GameStateInterface]
      val currentPlayer = mock[PlayerInterface]

      when(controller.getState).thenReturn(oldState)
      when(oldState.getCurrentPlayerIndex).thenReturn(1)
      when(newState.getCurrentPlayerIndex).thenReturn(1)
      when(oldState.currentPlayer).thenReturn(currentPlayer)
      when(currentPlayer.getHasCompletedFirstMove).thenReturn(false)

      when(controller.passTurn(oldState, false)).thenReturn((newState, "Turn passed"))
      when(currentPlayer.getCommandHistory).thenReturn(List("someCommand"))

      val tui = new Tui(controller)

      val out = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(out)) {
        tui.processGameInput("pass")
      }

      out.toString should include ("Failed to set new state.")
    }
  }
}