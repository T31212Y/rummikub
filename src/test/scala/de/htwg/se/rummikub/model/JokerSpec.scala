package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class JokerSpec extends AnyWordSpec {
  "Joker" should {
    "be red or black" in {
      val joker = Joker(Color.RED)
      List(Color.RED, Color.BLACK) should contain(joker.color)
    }

    "have a string representation" in {
      val joker = Joker(Color.RED)
      joker.toString should be(" \u001b[31mJ\u001b[0m")
    }
  }
}