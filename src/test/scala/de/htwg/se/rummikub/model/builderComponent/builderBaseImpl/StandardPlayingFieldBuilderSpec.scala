package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Table
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.Board
import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.TokenStack

import com.google.inject.Guice
import de.htwg.se.rummikub.RummikubModule

import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface

class StandardPlayingFieldBuilderSpec extends AnyWordSpec {
  "A StandardPlayingFieldBuilder" should {
    val injector = Guice.createInjector(new RummikubModule)
    val tokenStructureFactory = injector.getInstance(classOf[TokenStructureFactoryInterface])

    "set players, boards and innerField and build a PlayingField" in {
      val builder = new StandardPlayingFieldBuilder
      val players = List(Player("A", tokenStructureFactory = tokenStructureFactory), Player("B", tokenStructureFactory = tokenStructureFactory))
      val boards = List(Board(24, 14, 2, 2, "default", 10), Board(24, 14, 2, 2, "default", 10))
      val table = Table(2, 14)
      val stack = TokenStack(List()) 

      val pf = builder
        .setPlayers(players)
        .setBoards(boards)
        .setInnerField(table)
        .setStack(stack) 
        .build()

      pf.getPlayers shouldBe players
      pf.getBoards shouldBe boards
      pf.getInnerField shouldBe table
    }

    "throw an exception if players are not set" in {
      val builder = new StandardPlayingFieldBuilder
      val boards = List(Board(24, 14, 2, 2, "default", 10))
      val table = Table(1, 14)
      builder.setBoards(boards).setInnerField(table)
      an [IllegalStateException] should be thrownBy builder.build()
    }

    "throw an exception if boards are not set" in {
      val builder = new StandardPlayingFieldBuilder
      val players = List(Player("A", tokenStructureFactory = tokenStructureFactory))
      val table = Table(1, 14)
      builder.setPlayers(players).setInnerField(table)
      an [IllegalStateException] should be thrownBy builder.build()
    }

    "throw an exception if innerField is not set" in {
      val builder = new StandardPlayingFieldBuilder
      val players = List(Player("A", tokenStructureFactory = tokenStructureFactory))
      val boards = List(Board(24, 14, 2, 2, "default", 10))
      builder.setPlayers(players).setBoards(boards)
      an [IllegalStateException] should be thrownBy builder.build()
    }

    "throw an exception if stack is not set" in {
      val builder = new StandardPlayingFieldBuilder
      val players = List(Player("A", tokenStructureFactory = tokenStructureFactory))
      val boards = List(Board(24, 14, 2, 2, "default", 10))
      val table = Table(1, 14)
      builder.setPlayers(players).setBoards(boards).setInnerField(table)
      val exception = intercept[IllegalStateException] {
        builder.build()
      }
      exception.getMessage should include ("Stack not set")
    }
  }
}