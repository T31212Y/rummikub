package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TokenFactorySpec extends AnyWordSpec with Matchers {

  "The TokenFactory" should {

    "create a NumToken with correct values" in {
      val token = TokenFactory.createToken("NumToken", 5, Color.RED)
      token shouldBe a [NumToken]
      token.asInstanceOf[NumToken].color shouldBe Color.RED
      token.asInstanceOf[NumToken].number shouldBe 5
    }

    "create a Joker with correct color" in {
      val joker = TokenFactory.createToken("Joker", 0, Color.BLACK)
      joker shouldBe a [Joker]
      joker.asInstanceOf[Joker].color shouldBe Color.BLACK
    }

    "generate all tokens correctly" in {
      val tokens = TokenFactory.generateAllTokens()
      tokens.length shouldBe (13 * 4 * 2 + 2) 
      tokens.count(_.isInstanceOf[NumToken]) shouldBe 104
      tokens.count(_.isInstanceOf[Joker]) shouldBe 2

      val numTokenGroups = tokens.collect { case t: NumToken => (t.number, t.color) }
        .groupBy(identity)
        .mapValues(_.size)

      all (numTokenGroups.values) shouldBe 2

      val jokerColors = tokens.collect { case j: Joker => j.color }
      jokerColors.count(_ == Color.RED) shouldBe 1
      jokerColors.count(_ == Color.BLACK) shouldBe 1
    }
  }
}
