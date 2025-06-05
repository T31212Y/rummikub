package de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureMockImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

import de.htwg.se.rummikub.model.tokenComponent.tokenMockImpl.MockToken
import de.htwg.se.rummikub.model.tokenComponent.Color

class MockTokenStructureSpec extends AnyWordSpec {
  "A MockTokenStructure" should {
    "start empty or with initial tokens" in {
      val emptyStructure = new MockTokenStructure()
      emptyStructure.getTokens shouldBe empty

      val initial = List(MockToken(Some(1), Color.RED), MockToken(Some(2), Color.BLUE))
      val structureWithTokens = new MockTokenStructure(initial)
      structureWithTokens.getTokens shouldBe initial
    }

    "add tokens correctly" in {
      val structure = new MockTokenStructure()
      val token = MockToken(Some(3), Color.RED)
      structure.addToken(token)

      structure.getTokens should contain (token)
    }

    "remove tokens correctly" in {
      val token1 = MockToken(Some(4), Color.RED)
      val token2 = MockToken(Some(5), Color.BLUE)
      val structure = new MockTokenStructure(List(token1, token2))

      structure.removeToken(token1)
      structure.getTokens should not contain token1
      structure.getTokens should contain (token2)
    }

    "have a meaningful toString" in {
      val tokens = List(MockToken(Some(1), Color.RED))
      val structure = new MockTokenStructure(tokens)

      structure.toString should include ("MockToken")
      structure.toString should include ("tokens")
    }

    "validate structure correctly" in {
      val structure = new MockTokenStructure()
      structure.isValid shouldBe false

      val validTokens = List(MockToken(Some(1), Color.RED), MockToken(Some(2), Color.RED), MockToken(Some(3), Color.RED))
      val validStructure = new MockTokenStructure(validTokens)
      validStructure.isValid shouldBe true
    }

    "calculate points as sum of token numbers" in {
      val tokens = List(MockToken(Some(10), Color.RED), MockToken(Some(11), Color.RED), MockToken(Some(12), Color.RED))
      val structure = new MockTokenStructure(tokens)

      structure.points shouldBe 33
    }
  }
}