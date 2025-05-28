package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class TokenStackSpec extends AnyWordSpec {
    "TokenStack" should {
        "remove a token from the stack" in {
            val joker = Joker(Color.BLACK)
            val stack = TokenStack().removeToken(joker) 
            stack.tokens should not contain (joker)
        }

        "check if the stack is empty" in {
            val stack = TokenStack()
            stack.isEmpty should be(false)
        }

        "check the size of the stack" in {
            val stack = TokenStack()
            stack.size should be(106)
        }

        "be empty after removing all tokens" in {
            val stack = TokenStack().removeAllTokens()
            stack.isEmpty should be(true)
        }
        "have a string representation" in {
            val stack = TokenStack()
            val removedTokens = stack.removeMultipleTokens(104)
            stack.toString() should not contain (removedTokens.toString())
        }
        "draw a token from the stack" in {
            val stack = TokenStack()
            val token = stack.drawToken()
            token should not be null
            stack.size should be(105)
        }
        "draw multiple tokens from the stack" in {
            val stack = TokenStack()
            val tokens = stack.drawMultipleTokens(3)
            tokens.size should be(3)
            stack.size should be(103)
        }
    }
}