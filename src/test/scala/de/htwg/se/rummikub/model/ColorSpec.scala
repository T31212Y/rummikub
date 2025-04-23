package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class ColorSpec extends AnyWordSpec {
  "Color" should {
    "have a string representation" in {
      Color.RED.toString should be("\u001b[31m")
      Color.BLUE.toString should be("\u001b[34m")
      Color.GREEN.toString should be("\u001b[32m")
      Color.BLACK.toString should be("\u001b[30m")
    }
  }
}