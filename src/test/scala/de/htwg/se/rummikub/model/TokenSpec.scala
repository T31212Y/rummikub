package de.htwg.se.rummikub.model
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class TokenSpec extends AnyWordSpec {
    "Token" should {
        "have a valid color" in {
        val token = Token(1, "red")
        List("red", "blue", "green", "black") should contain (token.color)
        }

        "have a valid number" in {
            val token = Token(1, "red")
            token.number should (be >= 1 and be <= 13)
        }
    }
}