package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class TableSpec extends AnyWordSpec {
  "Table" should {
    "add a token" in {
      val table = Table(List())
      val token = Token(1, "red")
      table.addToken(token)
      table.getTokens should contain (List(token))
    }

    "remove a token" in {
      val token1 = Token(1, "red")
      val token2 = Token(2, "blue")
      val table = Table(List(List(token1), List(token2)))
      table.removeToken(token1)
      table.getTokens should not contain (List(token1))
    }

    "get tokens" in {
      val token1 = Token(1, "red")
      val token2 = Token(2, "blue")
      val table = Table(List(List(token1), List(token2)))
      table.getTokens should contain (List(token1))
      table.getTokens should contain (List(token2))
    }

    "have a correct string representation" in {
      val token1 = Token(1, "red")
      val token2 = Token(2, "blue")
      val table = Table(List(List(token1), List(token2)))
      table.toString should be ("1 red\n2 blue")
    }
  }

}