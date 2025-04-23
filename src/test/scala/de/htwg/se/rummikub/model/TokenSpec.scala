package de.htwg.se.rummikub.model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
class TokenSpec extends AnyWordSpec {
    "Token" should {
        "have a valid color" in {
        val token = Token(1, Color.RED)
        List(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK) should contain (token.color)
        }

        "have a valid number" in {
            val token = Token(1, Color.RED)
            token.number should (be >= 1 and be <= 13)
        }
    }
}