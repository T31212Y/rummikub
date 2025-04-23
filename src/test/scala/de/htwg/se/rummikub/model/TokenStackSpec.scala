package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class TokenStackSpec extends AnyWordSpec {
    "TokenStack" should {
        "remove a token from the stack" in {
            val joker = Joker(Color.BLACK)
            val stack = TokenStack().removeToken(joker) 
            stack should not contain joker
        }

        "check if the stack is empty" in {
            val stack = TokenStack()
            stack.isEmpty should be(true)
        }

        "check the size of the stack" in {
            val stack = TokenStack()
            stack.size should be(106)
        }

        "be empty after removing all tokens" in {
            val stack = TokenStack().removeAllTokens()
            stack.isEmpty should be(true)
        }
    }
}