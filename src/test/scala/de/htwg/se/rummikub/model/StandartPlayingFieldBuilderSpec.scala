package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class StandardPlayingFieldBuilderSpec extends AnyWordSpec {
  "A StandardPlayingFieldBuilder" should {
    "set players, boards and innerField and build a PlayingField" in {
      val builder = new StandardPlayingFieldBuilder
      val players = List(Player("A"), Player("B"))
      val boards = List(Board(24, 14, 2, 2, "default", 10), Board(24, 14, 2, 2, "default", 10))
      val table = Table(2, 14)

      val pf = builder
        .setPlayers(players)
        .setBoards(boards)
        .setInnerField(table)
        .build()

      pf.players shouldBe players
      pf.boards shouldBe boards
      pf.innerField shouldBe table
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
      val players = List(Player("A"))
      val table = Table(1, 14)
      builder.setPlayers(players).setInnerField(table)
      an [IllegalStateException] should be thrownBy builder.build()
    }

    "throw an exception if innerField is not set" in {
      val builder = new StandardPlayingFieldBuilder
      val players = List(Player("A"))
      val boards = List(Board(24, 14, 2, 2, "default", 10))
      builder.setPlayers(players).setBoards(boards)
      an [IllegalStateException] should be thrownBy builder.build()
    }
  }
}