package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class GroupSpec extends AnyWordSpec {
    "Group" should {
        "be created with a list of tokens" in {
            val token1 = "1:red"
            val token2 = "1:blue"
            val token3 = "1:green"
            val group = Group(List(token1, token2, token3))
            group.groupTokens should contain (token1)
            group.groupTokens should contain (token2)
            group.groupTokens should contain (token3)
        }

        "add a token" in {
            val row = Row(List())
            val token = Token(1, Color.RED)
            row.addToken(token)
            row.getTokens should contain (List(token))
        }

        "remove a token" in {
            val token1 = Token(11, Color.RED)
            val token2 = Token(11, Color.BLUE)
            val token3 = Token(11, Color.BLACK)
            val row = Row(List(token1, token2, token3).map(_.asInstanceOf[String]))
            row.removeToken(token1)
            row.getTokens should not contain (List(token1))
        }

        "get tokens" in {
            val token1 = Token(13, Color.GREEN)
            val token2 = Token(13, Color.RED)
            val token3 = Token(13, Color.BLUE)
            val row = Row(List(token1, token2, token3).map(_.asInstanceOf[String]))
            row.getTokens should contain (List(token1))
            row.getTokens should contain (List(token2))
            row.getTokens should contain (List(token3))
        }

        "have a string representation" in {
            val token1 = "7:red"
            val token2 = "7:blue"
            val token3 = "7:green"
            val group = Group(List(token1, token2, token3))
            group.toString should be (group.groupTokens.foreach(_.toString))
        }
    }
}