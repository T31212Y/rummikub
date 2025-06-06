package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.tokenComponent.Color

class NumTokenSpec extends AnyWordSpec {
  "A NumToken" should {
    "be created with a number and a color" in {
      val token = NumToken(1, Color.RED)
      token.number should be(1)
      token.color should be(Color.RED)
    }

    "have a string representation" in {
      val token = NumToken(1, Color.RED)
      token.toString should be("\u001B[31m 1\u001B[0m")
    }

    "return Some(number) for getNumber" in {
      val token = NumToken(5, Color.BLUE)
      token.getNumber shouldBe Some(5)
    }

    "return the correct color for getColor" in {
      val token = NumToken(7, Color.GREEN)
      token.getColor shouldBe Color.GREEN
    }
  }
}