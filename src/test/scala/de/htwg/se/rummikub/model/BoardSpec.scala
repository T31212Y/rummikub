package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class BoardSpec extends AnyWordSpec {
    "Board" should {
        "have a correct size" in {
            val board = new Board(15, 24, 2, 1, "up")
            board.size(board.boardELRP12_1) should be(49)
        }

        "have a correct string representation" in {
            val board = new Board(15, 24, 2, 1, "up")
            board.toString().replace("x", " ") should be(
                Vector(
                "|               |                                               |               |",
                "|               +-----------------------------------------------+               |",
                "|               |                                               |               |",
                "|               +-----------------------------------------------+               |"
                ).mkString("\n") + "\n"
            )
        }
    }
}