package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.tokenComponent.Color

class JokerSpec extends AnyWordSpec {
  "A Joker" should {
    "be created with a color" in {
      val joker = Joker(Color.RED)
      joker.color should be(Color.RED)
    }

    "have a string representation" in {
      val joker = Joker(Color.RED)
      joker.toString should be(" \u001B[31mJ\u001B[0m")
    }

    "return None for getNumber" in {
      val joker = Joker(Color.RED)
      joker.getNumber shouldBe None
    }

    "return the correct color for getColor" in {
      val joker = Joker(Color.BLACK)
      joker.getColor shouldBe Color.BLACK
    }
  }
}