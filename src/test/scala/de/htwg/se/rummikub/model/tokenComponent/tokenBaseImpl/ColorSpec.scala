package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

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

    "have a reset string" in {
      Color.RED.reset should be("\u001b[0m")
      Color.BLUE.reset should be("\u001b[0m")
      Color.GREEN.reset should be("\u001b[0m")
      Color.BLACK.reset should be("\u001b[0m")
    }

    "have a correct name" in {
      Color.RED.name should be("red")
      Color.BLUE.name should be("blue")
      Color.GREEN.name should be("green")
      Color.BLACK.name should be("black")
    }

    "have correct value" in {
      Color.RED.value should be(1)
      Color.BLUE.value should be(2)
      Color.GREEN.value should be(3)
      Color.BLACK.value should be(4)
    }
  }
}