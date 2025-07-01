package de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.rummikub.model.tokenComponent.Color
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.{NumToken, Joker}

class TokenFactorySpec extends AnyWordSpec with Matchers {

  "The TokenFactory" should {

    "create a NumToken with correct values" in {
      val tokenFactory = new StandardTokenFactory
      val token = tokenFactory.createNumToken(5, Color.RED)
      token shouldBe a [NumToken]
      token.asInstanceOf[NumToken].color shouldBe Color.RED
      token.asInstanceOf[NumToken].number shouldBe 5
    }

    "create a Joker with correct color" in {
      val tokenFactory = new StandardTokenFactory
      val joker = tokenFactory.createJoker(Color.BLACK)
      joker shouldBe a [Joker]
      joker.asInstanceOf[Joker].color shouldBe Color.BLACK
    }

    "generate all tokens correctly" in {
      val tokenFactory = new StandardTokenFactory
      val tokens = tokenFactory.generateAllTokens()
      tokens.length shouldBe (13 * 4 * 2 + 2)
      tokens.count(_.isInstanceOf[NumToken]) shouldBe 104
      tokens.count(_.isInstanceOf[Joker]) shouldBe 2

      val numTokenGroups = tokens.collect { case t: NumToken => (t.number, t.color) }
        .groupBy(identity)
        .view.mapValues(_.size).toMap

      all (numTokenGroups.values) shouldBe 2

      val jokerColors = tokens.collect { case j: Joker => j.color }
      jokerColors.count(_ == Color.RED) shouldBe 1
      jokerColors.count(_ == Color.BLACK) shouldBe 1
    }
  }
}
