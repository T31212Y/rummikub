package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Table
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Board
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.TokenStack
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.Color

import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import de.htwg.se.rummikub.RummikubModule

import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.{TableFactoryInterface, BoardFactoryInterface}
import de.htwg.se.rummikub.model.playerComponent.PlayerFactoryInterface
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.builderComponent.FieldDirectorInterface
import de.htwg.se.rummikub.controller.controllerComponent.GameStateInterface

class TwoPlayerModeSpec extends AnyWordSpec {
  "A TwoPlayerMode" should {
    val injector = Guice.createInjector(new RummikubModule)

    val tokenStackFactory = injector.getInstance(classOf[TokenStackFactoryInterface])
    val tokenStructureFactory = injector.getInstance(classOf[TokenStructureFactoryInterface])
    val tableFactory = injector.getInstance(classOf[TableFactoryInterface])
    val boardFactory = injector.getInstance(classOf[BoardFactoryInterface])
    val playerFactory = injector.getInstance(classOf[PlayerFactoryInterface])

    val playingFieldBuilder = injector.getInstance(classOf[PlayingFieldBuilderInterface])
    val director = injector.instance[FieldDirectorInterface](Names.named("TwoPlayer"))

    val playerNames = List("Azra", "Moritz")
    val mode = TwoPlayerMode(playerNames, tokenStackFactory, tableFactory, boardFactory, playerFactory, playingFieldBuilder, director)
    val players = mode.createPlayers

    "be created with two player names" in {
      mode.playerNames should be(playerNames)
    }

    "create two Player objects" in {
      players.size should be(2)
      players.map(_.getName) should contain allElementsOf playerNames
    }

    "return None if createPlayingField is called with empty player list" in {
      val pf = mode.createPlayingField(Nil)
      pf shouldBe None
    }

    "create a PlayingField with two players" in {
      val pf = mode.createPlayingField(players)
      pf.get.getPlayers.size should be(2)
      pf.get.getPlayers.map(_.getName) should contain allElementsOf playerNames
    }

    "update the PlayingField and keep two boards" in {
      val pf = mode.createPlayingField(players)
      val updatedOpt = mode.updatePlayingField(pf)
      updatedOpt.isDefined shouldBe true
      updatedOpt.get.getBoards.size should be(2)
    }

    "update the PlayingField and print error if less than 2 players" in {
      val pf = mode.createPlayingField(players)
      val pfWithOne = pf.map(f => f.updated(newPlayers = f.getPlayers.take(1), newBoards = f.getBoards, newInnerField = f.getInnerField))
      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(out) {
        val updated = mode.updatePlayingField(pfWithOne)
        updated.isDefined shouldBe true
        updated.get.getPlayers.size shouldBe 1
      }
      out.toString should include ("Not enough players to update.")
    }

    "update a single board for a player" in {
      val player = Player("Azra", tokenStructureFactory = tokenStructureFactory)
      val board = new Board(24, 14, 2, 2, "default", 10)
      val updated = mode.updateBoardSinglePlayer(player, board)
      updated.isDefined shouldBe true
    }

    "update multiple boards for players returns None" in {
      val board = new Board(24, 14, 2, 2, "default", 10)
      val updated = mode.updateBoardMultiPlayer(players, board)
      updated shouldBe None
    }

    "render the PlayingField as a string" in {
      val pf = mode.createPlayingField(players)
      val str = mode.renderPlayingField(pf)
      str should include ("Azra")
      str should include ("Moritz")
    }

    "render the PlayingField as a string with method render" in {
      val pf = mode.createPlayingField(players)

      val out = new java.io.ByteArrayOutputStream()
      Console.withOut(new java.io.PrintStream(out)) {
        mode.render(pf)
      }

      out.toString should include ("Azra")
      out.toString should include ("Moritz")
    }

    "renderPlayingField returns error string if None" in {
      mode.renderPlayingField(None) should include ("No playing field available")
    }

    "lineWithPlayerName should return a formatted line" in {
      val line = mode.lineWithPlayerName("*", 20, "Azra")
      line.isDefined shouldBe true
      line.get should include ("Azra")
    }

    "lineWith2PlayerNames should return None" in {
      mode.lineWith2PlayerNames("*", 20, "Azra", "Moritz") shouldBe None
    }

    "A player's board should split tokens correctly when more than cntTokens are present" in {
      val stack = tokenStackFactory.createShuffledStack
      val manyTokensTuple = stack.drawMultipleTokens(30)
      val player = Player("Azra", manyTokensTuple._1, tokenStructureFactory = tokenStructureFactory)
      val board = new Board(24, 14, 2, 2, "default", 10)

      val updatedOpt = mode.updateBoardSinglePlayer(player, board)
      updatedOpt.isDefined shouldBe true

      val updatedBoard = updatedOpt.get
      updatedBoard.getBoardELRP12_1 shouldEqual board.formatBoardRow(manyTokensTuple._1.take(mode.cntTokens))
      updatedBoard.getBoardELRP12_2 shouldEqual board.formatBoardRow(manyTokensTuple._1.drop(mode.cntTokens))
      updatedBoard.getBoardEUD shouldEqual board.createBoardFrameSingle(manyTokensTuple._1.take(mode.cntTokens))
    }
  }
}