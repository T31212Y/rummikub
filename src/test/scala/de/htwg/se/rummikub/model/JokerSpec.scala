package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class JokerSpec extends AnyWordSpec {
  "Joker" should {
    "be red or black" in {
      val joker = Joker("red")
      List("red", "black") should contain(joker.color)
    }

    "have a string representation" in {
      val joker = Joker("red")
      joker.toString should be("Joker red")
    }
  }
}