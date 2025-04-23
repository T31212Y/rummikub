package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class RowSpec extends AnyWordSpec {
  "Row" should {
    "be created with a list of tokens" in {
        val token1 = Token(7, Color.BLUE)
        val token2 = Token(8, Color.BLUE)
        val token3 = Token(9, Color.BLUE)
        val row = Row(List(token1, token2, token3).map(_.asInstanceOf[Token | Joker]))
    }

    "add a token" in {
      val row = Row(List())
      val token = Token(1, Color.RED)
      row.addToken(token)
      row.getTokens should contain (List(token))
    }

    "remove a token" in {
      val token1 = Token(11, Color.BLUE)
      val token2 = Token(12, Color.BLUE)
      val token3 = Token(13, Color.BLUE)
      val row = Row(List(token1, token2, token3).map(_.asInstanceOf[Token | Joker]))
      row.removeToken(token1)
      row.getTokens should not contain (List(token1))
    }

    "get tokens" in {
      val token1 = Token(13, Color.GREEN)
      val token2 = Token(1, Color.GREEN)
      val token3 = Token(2, Color.GREEN)
      val row = Row(List(token1, token2, token3).map(_.asInstanceOf[Token | Joker]))
      row.getTokens should contain (List(token1))
      row.getTokens should contain (List(token2))
      row.getTokens should contain (List(token3))
    }

    "have a correct string representation" in {
      val token1 = Token(5, Color.RED)
      val token2 = Token(6, Color.RED)
      val token3 = Token(7, Color.RED)
      val row = Row(List(token1, token2, token3).map(_.asInstanceOf[Token | Joker]))
      row.toString should be ("1 red\n2 red\n3 red")
    }
  }
}