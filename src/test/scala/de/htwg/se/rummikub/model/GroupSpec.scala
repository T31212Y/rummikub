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
            val tokenFactory = new StandardTokenFactory
            group.groupTokens should be (List(tokenFactory.createNumToken(1, Color.RED), tokenFactory.createNumToken(1, Color.BLUE), tokenFactory.createNumToken(1, Color.GREEN)))
        }

        "add a token" in {
            val token = "1:red"
            val group = Group(List(token))
            val tokenFactory = new StandardTokenFactory
            val tokenToAdd = tokenFactory.createNumToken(1, Color.RED)
            group.addToken(tokenToAdd)
            group.getTokens should contain (tokenToAdd)
        }

        "remove a token" in {
            val token1 = "11:red"
            val token2 = "11:blue"
            val token3 = "11:black"
            val tokenFactory = new StandardTokenFactory
            val tokenToRemove = tokenFactory.createNumToken(11, Color.RED)
            val group = Group(List(token1, token2, token3))
            group.removeToken(tokenToRemove)
            group.getTokens should not contain (tokenToRemove)
        }

        "get tokens" in {
            val token1 = "13:green"
            val token2 = "13:red"
            val token3 = "13:blue"
            val tokenFactory = new StandardTokenFactory
            val tokenToGet = tokenFactory.createNumToken(13, Color.GREEN)
            val group = Group(List(token1, token2, token3))
            group.getTokens should contain (tokenToGet)
        }

        "have a string representation" in {
            val token1 = "7:red"
            val token2 = "7:blue"
            val token3 = "J:red"
            val token4 = "J:black"
            val group = Group(List(token1, token2, token3, token4))
            group.toString should be (group.groupTokens.map(_.toString).mkString(" "))
        }
    }
}