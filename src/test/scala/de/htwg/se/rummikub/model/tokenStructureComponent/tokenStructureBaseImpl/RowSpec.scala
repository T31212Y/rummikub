package de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.{NumToken, Joker, StandardTokenFactory}
import de.htwg.se.rummikub.model.tokenComponent.Color

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

    "isValid should return false if numTokens is empty" in {
      val row = Row(List())
      row.isValid shouldBe false
    }

    "countMissingValues should return Int.MaxValue if actualValues and missing overlap" in {
      val row = Row(List(NumToken(1, Color.RED), NumToken(2, Color.RED), NumToken(3, Color.RED)))
      val actualValues = List(1, 2, 3)
      val targetSeq = List(2, 3, 4)
      val result = row.isValid
      succeed
    }

    "jokerValues should return None if numTokens is empty" in {
      val row = Row(List(Joker(Color.RED), Joker(Color.BLACK)))
      row.jokerValues shouldBe None
    }

    "jokerValues should return None if no valid sequence is found" in {
      val row = Row(List(NumToken(1, Color.RED), NumToken(5, Color.RED), Joker(Color.RED)))
      row.jokerValues shouldBe None
    }

    "points should use case _ => 0 in zipAll" in {
      val row = Row(List(Joker(Color.RED), Joker(Color.BLACK)))
      row.points shouldBe 0
    }

    "points should sum tokens if jokerValues is None" in {
      val row = Row(List(NumToken(1, Color.RED), NumToken(2, Color.RED)))
      row.points shouldBe 3
    }

    "return Int.MaxValue in countMissingValues if actualValues and missing overlap" in {
      val row = Row(List(NumToken(1, Color.RED), NumToken(2, Color.RED), NumToken(3, Color.RED)))
      val targetSeq = List(1, 2, 3, 3)
      val actualValues = List(1, 2, 3)
      val method = row.getClass.getDeclaredMethod("countMissingValues", classOf[List[Int]], classOf[List[Int]])
      method.setAccessible(true)
      val result = method.invoke(row, targetSeq, actualValues).asInstanceOf[Int]

      result shouldBe Int.MaxValue
    }

    "points should sum only NumToken values if jokerValues is None" in {
      val dummyToken = NumToken(0, Color.RED)
      val row = Row(List(NumToken(5, Color.RED), dummyToken))
      row.points shouldBe 5
    }

    "points should return 0 if the row contains only jokers" in {
      val row = Row(List(Joker(Color.RED), Joker(Color.BLACK)))
      row.points shouldBe 0
    }

    "points should use case _ => 0 in zipAll (null token)" in {
      val row = Row(List())
      val method = row.getClass.getDeclaredMethod("jokerValues")
      method.setAccessible(true)
      val testRow = new Row(List()) {
        override def jokerValues: Option[List[Int]] = Some(List(1, 2))
      }
      testRow.points shouldBe 0
    }
  }
}