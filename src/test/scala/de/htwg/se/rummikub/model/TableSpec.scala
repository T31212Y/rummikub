package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import tokenComponent.tokenBaseImpl.Joker

class TableSpec extends AnyWordSpec {
  "Table" should {
    "have an empty row" in {
      val table = Table(20, 20)
      table.emptyRow should be ("|                    |\n")
    }

    "have a correct string representation" in {
      val table = Table(5, 20)

      table.toString should be (
        Vector("|                    |",
               "|                    |",
               "|                    |",
               "|                    |",
               "|                    |"
              ).mkString("\n") + "\n"
      )
    }

    "format a row correctly with padding" in {
      val row = Row(List(NumToken(1, Color.BLUE), NumToken(2, Color.RED), NumToken(3, Color.GREEN)))
      val table = Table(5, 20, List(row.getTokens))

      table.toString should be (
        Vector("| \u001b[34m 1\u001b[0m \u001b[31m 2\u001b[0m \u001b[32m 3\u001b[0m           |",
                "|                    |",
                "|                    |",
                "|                    |",
                "|                    |"
              ).mkString("\n") + "\n"
      )
    }

    "not append empty rows when tokensOnTable is full" in {
      val row1 = Row(List(NumToken(1, Color.BLUE), NumToken(2, Color.RED), NumToken(3, Color.GREEN)))
      val row2 = Row(List(NumToken(4, Color.BLACK), NumToken(5, Color.BLUE), NumToken(6, Color.RED)))
      val table = Table(2, 20, List(row1.getTokens, row2.getTokens))

      table.toString should be (
        Vector("| \u001b[34m 1\u001b[0m \u001b[31m 2\u001b[0m \u001b[32m 3\u001b[0m           |",
                "| \u001b[30m 4\u001b[0m \u001b[34m 5\u001b[0m \u001b[31m 6\u001b[0m           |"
              ).mkString("\n")
      )
    }

    "append a new row to the table" in {
      val table = Table(5, 20)
      val newRow = Row(List(NumToken(1, Color.BLUE), NumToken(2, Color.RED), NumToken(3, Color.GREEN)))
      val updatedTable = table.add(newRow.getTokens)

      updatedTable.tokensOnTable.contains(newRow.getTokens).should(be(true))
      table.tokensOnTable should not contain (newRow.getTokens)
    }

    "remove ANSI escape codes from strings" in {
      val table = Table(5, 20)
      val colorString = "\u001B[34m1\u001B[0m"
      val stringWithoutColor = table.deleteColorCodes(colorString)

      stringWithoutColor should be ("1")
    }

    "remove tokens from the table" in {
      val row1 = Row(List(NumToken(1, Color.BLUE), NumToken(2, Color.RED), NumToken(3, Color.GREEN)))
      val row2 = Row(List(NumToken(4, Color.BLACK), NumToken(5, Color.BLUE), NumToken(6, Color.RED)))
      val table = Table(2, 20, List(row1.getTokens, row2.getTokens))

      val updatedTable = table.remove(List(NumToken(2, Color.RED), NumToken(5, Color.BLUE)))

      updatedTable.tokensOnTable.flatten.should(not contain (NumToken(2, Color.RED)))
      updatedTable.tokensOnTable.flatten.should(not contain (NumToken(5, Color.BLUE)))

      updatedTable.tokensOnTable.flatten.should(contain (NumToken(1, Color.BLUE)))
      updatedTable.tokensOnTable.flatten.should(contain (NumToken(3, Color.GREEN)))
      updatedTable.tokensOnTable.flatten.should(contain (NumToken(4, Color.BLACK)))
      updatedTable.tokensOnTable.flatten.should(contain (NumToken(6, Color.RED)))
    }

    "correctly remove both Joker and NumTokens from the table" in {
      val row = Row(List(Joker(Color.RED), NumToken(3, Color.GREEN), NumToken(5, Color.BLUE)))
      val table = Table(1, 20, List(row.getTokens))

      val updatedTable = table.remove(List(Joker(Color.RED), NumToken(3, Color.GREEN)))

      updatedTable.tokensOnTable.flatten.should(not contain (Joker(Color.RED)))
      updatedTable.tokensOnTable.flatten.should(not contain (NumToken(3, Color.GREEN)))

      updatedTable.tokensOnTable.flatten.should(contain (NumToken(5, Color.BLUE)))
    }

    "correctly parse and remove Joker tokens from the table" in {
      val row = Row(List(Joker(Color.BLACK), NumToken(5, Color.BLUE)))
      val table = Table(1, 20, List(row.getTokens))

      val updated = table.remove(List(Joker(Color.BLACK)))

      updated.tokensOnTable.flatten should not.contain(Joker(Color.BLACK))
      updated.tokensOnTable.flatten should contain (NumToken(5, Color.BLUE))
    }
    
    "correctly parse and remove number tokens from the table" in {
    val row = Row(List(NumToken(1, Color.BLACK), Joker(Color.BLACK)))
    val table = Table(1, 20, List(row.getTokens))

    val updated = table.remove(List(NumToken(1, Color.BLACK)))

    updated.tokensOnTable.flatten.should(not.contain(NumToken(1, Color.BLACK)))
    updated.tokensOnTable.flatten should contain (Joker(Color.BLACK))
    }

    "remove Joker and NumToken with specific colors from the table" in {
      val row = Row(List(Joker(Color.RED), NumToken(3, Color.GREEN), NumToken(5, Color.BLUE), Joker(Color.BLACK), NumToken(3, Color.RED)))
      val table = Table(1, 20, List(row.getTokens))

      val updatedTable = table.remove(List(Joker(Color.RED), NumToken(3, Color.GREEN)))

      updatedTable.tokensOnTable.flatten.should(not.contain(Joker(Color.RED)))
      updatedTable.tokensOnTable.flatten.should(not.contain(NumToken(3, Color.GREEN)))

      updatedTable.tokensOnTable.flatten should contain (NumToken(5, Color.BLUE))
      updatedTable.tokensOnTable.flatten should contain (Joker(Color.BLACK))
      updatedTable.tokensOnTable.flatten should contain (NumToken(3, Color.RED))
    }

    "remove both Joker and number tokens correctly" in {
      val row = Row(List(Joker(Color.RED), NumToken(3, Color.BLUE), NumToken(7, Color.BLACK)))
      val table = Table(1, 20, List(row.getTokens))

      val updated = table.remove(List(Joker(Color.RED), NumToken(3, Color.BLUE)))

      updated.tokensOnTable.flatten.should(contain (NumToken(7, Color.BLACK)))
      updated.tokensOnTable.length.should(be (1))
    }

    "remove Joker token from the table" in {
      val joker = Joker(Color.RED)
      val row = List(joker, NumToken(2, Color.BLUE))
      val table = Table(1, 20, List(row))

      val updatedTable = table.remove(List(Joker(Color.RED)))

      updatedTable.tokensOnTable.flatten should not contain joker
      updatedTable.tokensOnTable.flatten should contain (NumToken(2, Color.BLUE))
    }
    "remove NumToken from the table" in {
      val tokenToRemove = NumToken(7, Color.GREEN)
      val otherToken = NumToken(3, Color.BLUE)
      val row = List(tokenToRemove, otherToken)
      val table = Table(1, 20, List(row))

      val updatedTable = table.remove(List(NumToken(7, Color.GREEN)))

      updatedTable.tokensOnTable.flatten should not contain tokenToRemove
      updatedTable.tokensOnTable.flatten should contain (otherToken)
    }

    "getRow should return the correct row as Option" in {
      val row1 = Row(List(NumToken(1, Color.BLUE), NumToken(2, Color.RED), NumToken(3, Color.GREEN)))
      val row2 = Row(List(NumToken(4, Color.BLACK), NumToken(5, Color.BLUE), NumToken(6, Color.RED)))
      val table = Table(2, 20, List(row1.getTokens, row2.getTokens))
      table.getRow(0) shouldBe Some(row1.getTokens)
      table.getRow(1) shouldBe Some(row2.getTokens)
      table.getRow(2) shouldBe None
    }

    "getGroup should return the correct group as Option" in {
      val row1 = Row(List(NumToken(1, Color.BLUE), NumToken(2, Color.RED), NumToken(3, Color.GREEN)))
      val row2 = Row(List(NumToken(4, Color.BLACK), NumToken(5, Color.BLUE), NumToken(6, Color.RED)))
      val table = Table(2, 20, List(row1.getTokens, row2.getTokens))
      table.getGroup(0) shouldBe Some(row1.getTokens)
      table.getGroup(1) shouldBe Some(row2.getTokens)
      table.getGroup(2) shouldBe None
    }

    "toString should return only empty rows if table is empty" in {
      val table = Table(3, 10)
      table.toString shouldBe (("|          |\n" * 3))
    }

    "toString should return formatted rows and fill up with empty rows" in {
      val row = Row(List(NumToken(1, Color.BLUE), NumToken(2, Color.RED)))
      val table = Table(3, 15, List(row.getTokens))
      val lines = table.toString.split("\n")
      lines.length shouldBe 3
      lines.head should include ("1")
      lines.head should include ("2")
    }
  }
}