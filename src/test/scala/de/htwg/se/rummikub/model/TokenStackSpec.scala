package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class TokenStackSpec extends AnyWordSpec {
    "TokenStackSpec" should {
        "remove a token from the stack" in {
            val token1 = Token(1, "red")
            val token2 = Token(2, "blue")
            val token3 = Token(3, "green")
            val joker = Joker("black")

            var stack = TokenStack(List(token1, token2, token3, joker))

            stack.removeToken(token2)

            stack.tokens should contain theSameElementsInOrderAs List(token1, token3, joker)
        }

        "check if the stack is empty" in {
            val stack = TokenStack(List())
            stack.isEmpty should be(true)
        }

        "check the size of the stack" in {
            val token1 = Token(1, "red")
            val token2 = Token(2, "blue")
            val joker = Joker("black")

            var stack = TokenStack(List(token1, token2, joker))

            stack.size should be(3)
        }
    }
    "TokenStack" should {
        "be empty after removing all tokens" in {
            val token1 = Token(1, "red")
            val token2 = Token(2, "blue")
            val joker = Joker("black")

            var stack = TokenStack(List(token1, token2, joker))

            stack.removeToken(token1)
            stack.removeToken(token2)
            stack.removeToken(joker)

            stack.isEmpty should be(true)
        }
    }
}