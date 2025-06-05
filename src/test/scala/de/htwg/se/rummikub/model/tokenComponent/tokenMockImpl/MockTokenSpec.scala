package de.htwg.se.rummikub.model.tokenComponent.tokenMockImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.tokenComponent.Color

class MockTokenSpec extends AnyWordSpec {

  "A MockToken" should {
    "return the correct number and color" in {
      val token = MockToken(Some(5), Color.RED)

      token.getNumber should contain (5)
      token.getColor shouldBe Color.RED
      token.toString should include ("5")
    }

    "handle None as number correctly" in {
      val token = MockToken(None, Color.BLUE)

      token.getNumber shouldBe empty
      token.getColor shouldBe Color.BLUE
    }
  }
}
