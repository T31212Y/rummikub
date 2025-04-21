package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class ColorSpec extends AnyWordSpec {
  "Color" should {
    "have a string representation" in {
      Color.RED.toString should be("RED")
      Color.BLUE.toString should be("BLUE")
      Color.GREEN.toString should be("GREEN")
      Color.BLACK.toString should be("BLACK")
    }
  }
}