package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class RowSpec extends AnyWordSpec {
  "Row" should {
    "be created with a list of tokens" in {
        val tokenFactory = new StandardTokenFactory
        val token1 = tokenFactory.createNumToken(7, Color.BLUE)
        val token2 = tokenFactory.createNumToken(8, Color.BLUE)
        val token3 = tokenFactory.createNumToken(9, Color.BLUE)
        val row = Row(List(token1, token2, token3))
        row.getTokens should be (List(token1, token2, token3))
    }

    "add a token" in {
      val tokenFactory = new StandardTokenFactory
      val token = tokenFactory.createNumToken(1, Color.RED)
      val row = Row(List(token))
      val tokenToAdd = tokenFactory.createNumToken(1, Color.RED)
      row.addToken(tokenToAdd)
      row.getTokens should contain (tokenToAdd)
    }

    "remove a token" in {
      val tokenFactory = new StandardTokenFactory
      val token1 = tokenFactory.createNumToken(11, Color.BLUE)
      val token2 = tokenFactory.createNumToken(12, Color.BLUE)
      val token3 = tokenFactory.createNumToken(13, Color.BLUE)
      val row = Row(List(token1, token2, token3))
      val tokenToRemove = tokenFactory.createNumToken(13, Color.BLUE)
      row.removeToken(tokenToRemove)
      row.getTokens should not contain (tokenToRemove)
    }

    "get tokens" in {
      val tokenFactory = new StandardTokenFactory
      val token1 = tokenFactory.createNumToken(13, Color.GREEN)
      val token2 = tokenFactory.createNumToken(1, Color.GREEN)
      val token3 = tokenFactory.createNumToken(2, Color.GREEN)
      val row = Row(List(token1, token2, token3))
      val tokenToGet = tokenFactory.createNumToken(13, Color.GREEN)
      row.getTokens should contain (tokenToGet)
    }

    "have a correct string representation" in {
      val tokenFactory = new StandardTokenFactory
      val token1 = tokenFactory.createNumToken(5, Color.RED)
      val token2 = tokenFactory.createNumToken(6, Color.RED)
      val token3 = tokenFactory.createJoker(Color.RED)
      val token4 = tokenFactory.createJoker(Color.BLACK)
      val row = Row(List(token1, token2, token3, token4))
      row.toString should be (row.getTokens.map(_.toString).mkString(" "))
    }

    "change string list to token list" in {
      val tokenFactory = new StandardTokenFactory
      val token1 = tokenFactory.createNumToken(5, Color.RED)
      val token2 = tokenFactory.createNumToken(6, Color.RED)
      val token3 = tokenFactory.createJoker(Color.RED)
      val token4 = tokenFactory.createJoker(Color.BLACK)
      val row = Row(List(token1, token2, token3, token4))
      row.toString should be (row.getTokens.map(_.toString).mkString(" "))
    }
  }
}