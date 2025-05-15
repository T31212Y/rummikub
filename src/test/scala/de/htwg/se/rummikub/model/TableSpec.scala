package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

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
      val row = Row(List("1:blue", "2:red", "3:green"))
      val table = Table(5, 20, List(row.rowTokens))

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
      val row1 = Row(List("1:blue", "2:red", "3:green"))
      val row2 = Row(List("4:black", "5:blue", "6:red"))
      val table = Table(2, 20, List(row1.rowTokens, row2.rowTokens))

      table.toString should be (
        Vector("| \u001b[34m 1\u001b[0m \u001b[31m 2\u001b[0m \u001b[32m 3\u001b[0m           |",
                "| \u001b[30m 4\u001b[0m \u001b[34m 5\u001b[0m \u001b[31m 6\u001b[0m           |"
              ).mkString("\n")
      )
    }

    "append a new row to the table" in {
      val table = Table(5, 20)
      val newRow = Row(List("1:blue", "2:red", "3:green"))
      val updatedTable = table.add(newRow.rowTokens)

      updatedTable.tokensOnTable should contain (newRow.rowTokens)
      table.tokensOnTable should not contain (newRow.rowTokens)
    }

    "remove ANSI escape codes from strings" in {
      val table = Table(5, 20)
      val colorString = "\u001B[34m1\u001B[0m"
      val stringWithoutColor = table.deleteColorCodes(colorString)

      stringWithoutColor should be ("1")
    }

    "remove tokens from the table" in {
      val row1 = Row(List("1:blue", "2:red", "3:green"))
      val row2 = Row(List("4:black", "5:blue", "6:red"))
      val table = Table(2, 20, List(row1.rowTokens, row2.rowTokens))

      val updatedTable = table.remove(List("2:red", "5:blue"))

      updatedTable.tokensOnTable.flatten should not contain (NumToken(2, Color.RED))
      updatedTable.tokensOnTable.flatten should not contain (NumToken(5, Color.BLUE))

      updatedTable.tokensOnTable.flatten should contain (NumToken(1, Color.BLUE))
      updatedTable.tokensOnTable.flatten should contain (NumToken(3, Color.GREEN))
      updatedTable.tokensOnTable.flatten should contain (NumToken(4, Color.BLACK))
      updatedTable.tokensOnTable.flatten should contain (NumToken(6, Color.RED))
    }

    "correctly remove both Joker and NumTokens from the table" in {
      val row = Row(List("J:red", "3:green", "5:blue"))
      val table = Table(1, 20, List(row.rowTokens))

      val updatedTable = table.remove(List("J:red", "3:green"))

      updatedTable.tokensOnTable.flatten should not contain (Joker(Color.RED))
      updatedTable.tokensOnTable.flatten should not contain (NumToken(3, Color.GREEN))

      updatedTable.tokensOnTable.flatten should contain (NumToken(5, Color.BLUE))
    }

    "correctly parse and remove Joker tokens from the table" in {
      val row = Row(List("J:black", "5:blue"))
      val table = Table(1, 20, List(row.rowTokens))

      val updated = table.remove(List("J:black"))

      updated.tokensOnTable.flatten should not contain (Joker(Color.BLACK))
      updated.tokensOnTable.flatten should contain (NumToken(5, Color.BLUE))
    }
    
    "correctly parse and remove number tokens from the table" in {
    val row = Row(List("1:black", "J:black"))
    val table = Table(1, 20, List(row.rowTokens))

    val updated = table.remove(List("1:black"))

    updated.tokensOnTable.flatten should not contain (NumToken(1, Color.BLACK))
    updated.tokensOnTable.flatten should contain (Joker(Color.BLACK))
    }

    "remove Joker and NumToken with specific colors from the table" in {
      val row = Row(List("J:red", "3:green", "5:blue", "J:black", "3:red"))
      val table = Table(1, 20, List(row.rowTokens))

      val updatedTable = table.remove(List("J:red", "3:green"))

      updatedTable.tokensOnTable.flatten should not contain (Joker(Color.RED))
      updatedTable.tokensOnTable.flatten should not contain (NumToken(3, Color.GREEN))

      updatedTable.tokensOnTable.flatten should contain (NumToken(5, Color.BLUE))
      updatedTable.tokensOnTable.flatten should contain (Joker(Color.BLACK))
      updatedTable.tokensOnTable.flatten should contain (NumToken(3, Color.RED))
    }

    "remove both Joker and number tokens correctly" in {
      val row = Row(List("J:red", "3:blue", "7:black"))
      val table = Table(1, 20, List(row.rowTokens))

      val updated = table.remove(List("J:red", "3:blue"))

      updated.tokensOnTable.flatten should contain (NumToken(7, Color.BLACK))
      updated.tokensOnTable.length should be (1)
    }

    "remove Joker token from the table" in {
      val joker = Joker(Color.RED)
      val row = List(joker, NumToken(2, Color.BLUE))
      val table = Table(1, 20, List(row))

      val updatedTable = table.remove(List("J:red"))

      updatedTable.tokensOnTable.flatten should not contain joker
      updatedTable.tokensOnTable.flatten should contain (NumToken(2, Color.BLUE))
    }
    "remove NumToken from the table" in {
      val tokenToRemove = NumToken(7, Color.GREEN)
      val otherToken = NumToken(3, Color.BLUE)
      val row = List(tokenToRemove, otherToken)
      val table = Table(1, 20, List(row))

      val updatedTable = table.remove(List("7:green"))

      updatedTable.tokensOnTable.flatten should not contain tokenToRemove
      updatedTable.tokensOnTable.flatten should contain (otherToken)
    }
  }
}