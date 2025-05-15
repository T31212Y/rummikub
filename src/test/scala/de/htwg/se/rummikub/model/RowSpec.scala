package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class RowSpec extends AnyWordSpec {
  "Row" should {
    "be created with a list of tokens" in {
        val token1 = "7:blue"
        val token2 = "8:blue"
        val token3 = "9:blue"
        val row = Row(List(token1, token2, token3))
        val tokenFactory = new StandardTokenFactory
        row.rowTokens should be (List(tokenFactory.createNumToken(7, Color.BLUE), tokenFactory.createNumToken(8, Color.BLUE), tokenFactory.createNumToken(9, Color.BLUE)))
    }

    "add a token" in {
      val token = "1:red"
      val row = Row(List(token))
      val tokenFactory = new StandardTokenFactory
      val tokenToAdd = tokenFactory.createNumToken(1, Color.RED)
      row.addToken(tokenToAdd)
      row.getTokens should contain (tokenToAdd)
    }

    "remove a token" in {
      val token1 = "11:blue"
      val token2 = "12:blue"
      val token3 = "13:blue"
      val row = Row(List(token1, token2, token3))
      val tokenFactory = new StandardTokenFactory
      val tokenToRemove = tokenFactory.createNumToken(13, Color.BLUE)
      row.removeToken(tokenToRemove)
      row.getTokens should not contain (tokenToRemove)
    }

    "get tokens" in {
      val token1 = "13:green"
      val token2 = "1:green"
      val token3 = "2:green"
      val row = Row(List(token1, token2, token3))
      val tokenFactory = new StandardTokenFactory
      val tokenToGet = tokenFactory.createNumToken(13, Color.GREEN)
      row.getTokens should contain (tokenToGet)
    }

    "have a correct string representation" in {
      val token1 = "5:red"
      val token2 = "6:red"
      val token3 = "J:red"
      val token4 = "J:black"
      val row = Row(List(token1, token2, token3, token4))
      row.toString should be (row.rowTokens.map(_.toString).mkString(" "))
    }

    "change string list to token list" in {
      val tokenStrings = List("1:red", "2:blue", "3:green", "4:black")
      val row = Row(tokenStrings)
      val tokenFactory = new StandardTokenFactory
      row.changeStringListToTokenList(tokenStrings) should be (List(tokenFactory.createNumToken(1, Color.RED), tokenFactory.createNumToken(2, Color.BLUE), tokenFactory.createNumToken(3, Color.GREEN), tokenFactory.createNumToken(4, Color.BLACK)))
    }
  }
}