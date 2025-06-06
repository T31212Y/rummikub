package de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.{NumToken, Joker, StandardTokenFactory}
import de.htwg.se.rummikub.model.tokenComponent.Color


class GroupSpec extends AnyWordSpec {
    "Group" should {
        val tokenFactory = new StandardTokenFactory
        val tokenRed1 = tokenFactory.createNumToken(1, Color.RED)
        val tokenBlue1 = tokenFactory.createNumToken(1, Color.BLUE)
        val tokenGreen1 = tokenFactory.createNumToken(1, Color.GREEN)
        val tokenJokerRed = tokenFactory.createJoker(Color.RED)
        val tokenJokerBlack = tokenFactory.createJoker(Color.BLACK)

        "be created with a list of tokens" in {
            val group = Group(List(tokenRed1, tokenBlue1, tokenGreen1))
            group.getTokens should be (List(tokenRed1, tokenBlue1, tokenGreen1))
        }

        "add a token" in {
            val token = tokenFactory.createNumToken(1, Color.RED)
            val group = Group(List(token))
            val tokenToAdd = tokenFactory.createNumToken(1, Color.BLUE)
            group.addToken(tokenToAdd)
            group.getTokens should contain (tokenToAdd)
        }

        "remove a token" in {
            val token1 = tokenFactory.createNumToken(11, Color.RED)
            val token2 = tokenFactory.createNumToken(11, Color.BLUE)
            val token3 = tokenFactory.createNumToken(11, Color.BLACK)
            val group = Group(List(token1, token2, token3))
            group.removeToken(token1)
            group.getTokens should not contain token1
        }

        "get tokens" in {
            val group = Group(List(tokenRed1, tokenBlue1, tokenGreen1))
            group.getTokens should contain (tokenRed1)
        }

        "have a string representation" in {
            val group = Group(List(tokenRed1, tokenBlue1, tokenJokerRed, tokenJokerBlack))
            group.toString should be (group.getTokens.map(_.toString).mkString(" "))
        }

        "be invalid if there are no NumTokens (only Jokers)" in {
            val group = Group(List(tokenJokerRed, tokenJokerBlack))
            group.isValid shouldBe false
        }

        "be invalid if there are more Jokers than available colors" in {
            val token1 = tokenFactory.createNumToken(5, Color.RED)
            val token2 = tokenFactory.createNumToken(5, Color.BLUE)
            val token3 = tokenFactory.createNumToken(5, Color.GREEN)
            val joker1 = tokenFactory.createJoker(Color.RED)
            val joker2 = tokenFactory.createJoker(Color.BLACK)
            val group = Group(List(token1, token2, token3, joker1, joker2))
            group.isValid shouldBe false
        }


        "return the joker value when there is exactly one NumToken" in {
            val token = tokenFactory.createNumToken(7, Color.BLUE)
            val group = Group(List(token, tokenJokerRed))
            group.jokerValues shouldBe Some(7)
        }

        "calculate points using joker values" in {
            val token = tokenFactory.createNumToken(10, Color.GREEN)
            val group = Group(List(token, tokenJokerRed))
            group.points shouldBe 20
        }

        "jokerValues should return None if NumTokens have different values" in {
            val group = Group(List(NumToken(7, Color.RED), NumToken(8, Color.BLUE), Joker(Color.BLACK)))
            group.jokerValues shouldBe None
        }
    }
}
