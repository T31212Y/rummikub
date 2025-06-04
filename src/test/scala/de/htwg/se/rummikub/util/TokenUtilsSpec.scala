package de.htwg.se.rummikub.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.Joker

class TokenUtilsSpec extends AnyWordSpec {
  "TokenUtils" should {
    "match identical NumTokens" in {
      val t1 = NumToken(5, Color.RED)
      val t2 = NumToken(5, Color.RED)
      TokenUtils.tokensMatch(t1, t2) shouldBe true
    }

    "not match NumTokens with different numbers" in {
      val t1 = NumToken(5, Color.RED)
      val t2 = NumToken(6, Color.RED)
      TokenUtils.tokensMatch(t1, t2) shouldBe false
    }

    "not match NumTokens with different colors" in {
      val t1 = NumToken(5, Color.RED)
      val t2 = NumToken(5, Color.BLUE)
      TokenUtils.tokensMatch(t1, t2) shouldBe false
    }

    "match two Jokers" in {
      TokenUtils.tokensMatch(Joker(Color.RED), Joker(Color.BLACK)) shouldBe true
    }

    "not match Joker and NumToken" in {
      TokenUtils.tokensMatch(Joker(Color.RED), NumToken(5, Color.RED)) shouldBe false
    }
  }
}

