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
      player.toString should be("Anki")
    }
  }
}