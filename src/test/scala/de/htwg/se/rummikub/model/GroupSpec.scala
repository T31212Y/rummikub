package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class GroupSpec extends AnyWordSpec {
    "Group" should {
        "be created with a list of tokens" in {
            val token1 = Token(1, "red")
            val token2 = Token(1, "blue")
            val token3 = Token(1, "green")
            val group = Group(List(token1, token2, token3))
            group.tokens should contain (token1)
            group.tokens should contain (token2)
            group.tokens should contain (token3)
        }

        "add a token" in {
            val row = Row(List())
            val token = Token(1, "red")
            row.addToken(token)
            row.getTokens should contain (List(token))
        }

        "remove a token" in {
            val token1 = Token(11, "red")
            val token2 = Token(11, "blue")
            val token3 = Token(11, "black")
            val row = Row(List(token1, token2, token3).map(_.asInstanceOf[Token | Joker]))
            row.removeToken(token1)
            row.getTokens should not contain (List(token1))
        }

        "get tokens" in {
            val token1 = Token(13, "green")
            val token2 = Token(13, "red")
            val token3 = Token(13, "blue")
            val row = Row(List(token1, token2, token3).map(_.asInstanceOf[Token | Joker]))
            row.getTokens should contain (List(token1))
            row.getTokens should contain (List(token2))
            row.getTokens should contain (List(token3))
        }

        "have a string representation" in {
            val token1 = Token(7, "red")
            val token2 = Token(7, "blue")
            val token3 = Token(7, "green")
            val group = Group(List(token1, token2, token3))
            group.toString should be ("7 red\n7 blue\n7 green")
        }
    }
}