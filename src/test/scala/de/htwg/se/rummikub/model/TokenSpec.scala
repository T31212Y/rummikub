package de.htwg.se.rummikub.model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
class TokenSpec extends AnyWordSpec {
    "Token" should {
        "have a valid color" in {
            val tokenFactory = new StandardTokenFactory
            val token = tokenFactory.createNumToken(1, Color.RED)
            token match {
                case numToken: NumToken =>
                    Color.values should contain (numToken.color)
                case _ => fail("Expected a NumToken")
            }
        }

        "have a valid number (1 to 13)" in {
            val tokenFactory = new StandardTokenFactory
            val token = tokenFactory.createNumToken(1, Color.RED)
            token match {
                case numToken: NumToken =>
                    numToken.number should (be >= 1 and be <= 13)
                case _ => fail("Expected a NumToken")
            }
        }
    }
}