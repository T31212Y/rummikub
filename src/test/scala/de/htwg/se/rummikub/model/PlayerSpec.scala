package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class PlayerSpec extends AnyWordSpec {
  "Player" should {
    "have a name" in {
      val player = Player("Anki")
      player.name should be("Anki")
    }

    "have a string representation" in {
      val player = Player("Anki")
      player.toString should be("Player: Anki")
    }

    "validate first move correctly" in {
      val player = Player("Anki", firstMoveTokens = List(NumToken(10, Color.RED), NumToken(10, Color.BLUE), NumToken(10, Color.GREEN)))
      player.validateFirstMove() should be(true)

      val player2 = Player("Anki", firstMoveTokens = List(NumToken(1, Color.RED), NumToken(2, Color.RED), Joker(Color.BLACK), NumToken(4, Color.RED)))
      player2.validateFirstMove() should be(false)
    }

    "add tokens to first move" in {
      val player = Player("Anki")
      val newTokens = List(NumToken(10, Color.RED), NumToken(10, Color.BLUE), NumToken(10, Color.GREEN))
      val updatedPlayer = player.addToFirstMoveTokens(newTokens)
      updatedPlayer.firstMoveTokens should contain theSameElementsAs newTokens
    }
  }
}