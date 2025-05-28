package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class GroupSpec extends AnyWordSpec {
    "Group" should {
        "be created with a list of tokens" in {
            val tokenFactory = new StandardTokenFactory
            val token1 = tokenFactory.createNumToken(1, Color.RED)
            val token2 = tokenFactory.createNumToken(1, Color.BLUE)
            val token3 = tokenFactory.createNumToken(1, Color.GREEN)
            val group = Group(List(token1, token2, token3))
            group.getTokens should be (List(token1, token2, token3))
        }

        "add a token" in {
            val tokenFactory = new StandardTokenFactory
            val token = tokenFactory.createNumToken(1, Color.RED)
            val group = Group(List(token))
            val tokenToAdd = tokenFactory.createNumToken(1, Color.RED)
            group.addToken(tokenToAdd)
            group.getTokens should contain (tokenToAdd)
        }

        "remove a token" in {
            val tokenFactory = new StandardTokenFactory
            val token1 = tokenFactory.createNumToken(11, Color.RED)
            val token2 = tokenFactory.createNumToken(11, Color.BLUE)
            val token3 = tokenFactory.createNumToken(11, Color.BLACK)
            val tokenToRemove = tokenFactory.createNumToken(11, Color.RED)
            val group = Group(List(token1, token2, token3))
            group.removeToken(tokenToRemove)
            group.getTokens should not contain (tokenToRemove)
        }

        "get tokens" in {
            val tokenFactory = new StandardTokenFactory
            val token1 = tokenFactory.createNumToken(13, Color.GREEN)
            val token2 = tokenFactory.createNumToken(13, Color.RED)
            val token3 = tokenFactory.createNumToken(13, Color.BLUE)
            val tokenToGet = tokenFactory.createNumToken(13, Color.GREEN)
            val group = Group(List(token1, token2, token3))
            group.getTokens should contain (tokenToGet)
        }

        "have a string representation" in {
            val tokenFactory = new StandardTokenFactory
            val token1 = tokenFactory.createNumToken(7, Color.RED)
            val token2 = tokenFactory.createNumToken(7, Color.BLUE)
            val token3 = tokenFactory.createJoker(Color.RED)
            val token4 = tokenFactory.createJoker(Color.BLACK)
            val group = Group(List(token1, token2, token3, token4))
            group.toString should be (group.getTokens.map(_.toString).mkString(" "))
        }
    }
}