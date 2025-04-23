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
               ).mkString("\n") + "\n")
    }
  }

}