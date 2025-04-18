package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class RowSpec extends AnyWordSpec {
  "Row" should {
    "be created with a list of tokens" in {
        val token1 = Token(7, "blue")
        val token2 = Token(8, "blue")
        val token3 = Token(9, "blue")
        val row = Row(List(token1, token2, token3).map(_.asInstanceOf[Token | Joker]))
    }

    "add a token" in {
      val row = Row(List())
      val token = Token(1, "red")
      row.addToken(token)
      row.getTokens should contain (List(token))
    }

    "remove a token" in {
      val token1 = Token(11, "blue")
      val token2 = Token(12, "blue")
      val token3 = Token(13, "blue")
      val row = Row(List(token1, token2, token3).map(_.asInstanceOf[Token | Joker]))
      row.removeToken(token1)
      row.getTokens should not contain (List(token1))
    }

    "get tokens" in {
      val token1 = Token(13, "green")
      val token2 = Token(1, "green")
      val token3 = Token(2, "green")
      val row = Row(List(token1, token2, token3).map(_.asInstanceOf[Token | Joker]))
      row.getTokens should contain (List(token1))
      row.getTokens should contain (List(token2))
      row.getTokens should contain (List(token3))
    }

    "have a correct string representation" in {
      val token1 = Token(5, "red")
      val token2 = Token(6, "red")
      val token3 = Token(7, "red")
      val row = Row(List(token1, token2, token3).map(_.asInstanceOf[Token | Joker]))
      row.toString should be ("1 red\n2 red\n3 red")
    }
  }
}