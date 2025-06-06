package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.Joker
import de.htwg.se.rummikub.model.tokenComponent.Color
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.StandardTokenFactory

class BoardSpec extends AnyWordSpec {
    "Board" should {
        "have a correct size" in {
            val board = new Board(15, 24, 2, 1, "up")
            board.size(board.boardELRP12_1) should be(49)
        }
        
        "format an empty board row correctly" in {
            val board = new Board(15, 24, 2, 1, "up")
            val emptyRow = board.formatEmptyBoardRow(10)
            emptyRow should be("|            |")
        }

        "wrap a single board row correctly" in {
            val board = new Board(2, 24, 2, 1, "up")
            val wrapped = board.wrapBoardRowSingle("[123]")
            wrapped should be("|  [123]  |")
        }

        "wrap a double board row correctly" in {
            val board = new Board(2, 24, 2, 2, "up")
            val wrapped = board.wrapBoardRowDouble("[123]", "[456]")
            wrapped should be("|  [123]  [456]  |")
        }

        "wrap a single to double board row correctly" in {
            val board = new Board(2, 24, 3, 1, "down")
            val wrapped = board.wrapBoardRowSingleToDouble("[123]", 90)
            wrapped.length should be (101) 
        }

        "create a board frame for double rows" in {
            val board = new Board(2, 24, 2, 2, "up")
            val frame = board.createBoardFrameFromStringDouble(5, 7)
            frame should include ("+-----+")
            frame should include ("+-------+")
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

        "have correct toString for amtBoardsInRow = 2 and dest = up" in {
            val board = new Board(15, 24, 2, 2, "up")
            val str = board.toString().replace("x", " ").replace("y", " ")
            str should include ("|")
            str should include ("+")
        }

        "have correct toString for amtBoardsInRow = 1, dest = down, amtPlayers != 3" in {
            val board = new Board(15, 24, 2, 1, "down")
            val str = board.toString().replace("x", " ")
            str should include ("|")
            str should include ("+")
        }

        "have correct toString for amtBoardsInRow = 1, dest = down, amtPlayers == 3" in {
            val board = new Board(15, 24, 3, 1, "down")
            val str = board.toString().replace("x", " ")
            str should include ("|")
            str should include ("+")
        }

        "have correct toString for amtBoardsInRow = 2, dest = down" in {
            val board = new Board(15, 24, 2, 2, "down")
            val str = board.toString().replace("x", " ").replace("y", " ")
            str should include ("|")
            str should include ("+")
        }

        "should create a single board frame with tokens correctly" in {
            val board = new Board(2, 5, 2, 1, "up")
            val tokenFactory = new StandardTokenFactory
            val row = List.fill(5)(tokenFactory.createJoker(Color.RED))
            val frame = board.createBoardFrameSingle(row)
            frame should include("|  +----------------+  |")
        }


        "should create a single board frame with jokers correctly" in {
            val board = new Board(2, 5, 2, 1, "up")
            val row = List.fill(5)(Joker(Color.RED))
            val frame = board.createBoardFrameSingle(row)
            frame should include("|  +----------------+  |")
        }

        "should create a double board frame with tokens correctly" in {
            val board = new Board(2, 5, 2, 2, "up")
            val tokenFactory = new StandardTokenFactory
            val row1 = List.fill(5)(tokenFactory.createNumToken(1, Color.RED))
            val row2 = List.fill(5)(tokenFactory.createNumToken(2, Color.RED))
            val frame = board.createBoardFrameDouble(row1, row2)
            frame should include("|  +----------------+  +----------------+  |")

        }

        "should create a double board frame with jokers correctly" in {
            val board = new Board(2, 5, 2, 2, "up")
            val row1 = List.fill(5)(Joker(Color.RED))
            val row2 = List.fill(5)(Joker(Color.BLACK))
            val frame = board.createBoardFrameDouble(row1, row2)
            frame should include("|  +----------------+  +----------------+  |")
        }

        "should create an empty board frame correctly" in {
            val board = new Board(2, 5, 2, 1, "up")
            val row = List()
            val frame = board.createBoardFrameSingle(row)
            frame should include("|  +----------------+  |")
        }

        "handle unexpected amtBoardsInRow or dest gracefully" in {
            val board = new Board(15, 24, 2, 3, "left")
            board.toString() should be ("")
        }

        "return the correct cntEdgeSpaces for getCntEdgeSpaces" in {
            val board = new Board(15, 24, 2, 1, "up")
            board.getCntEdgeSpaces shouldBe 15
        }

        "return the correct amtTokens for getAmtTokens" in {
            val board = new Board(15, 24, 2, 1, "up")
            board.getAmtTokens shouldBe 24
        }

        "return the correct amtPlayers for getAmtPlayers" in {
            val board = new Board(15, 24, 3, 1, "up")
            board.getAmtPlayers shouldBe 3
        }

        "return the correct amtBoardsInRow for getAmtBoardsInRow" in {
            val board = new Board(15, 24, 2, 2, "up")
            board.getAmtBoardsInRow shouldBe 2
        }

        "return the correct dest for getDest" in {
            val board = new Board(15, 24, 2, 1, "down")
            board.getDest shouldBe "down"
        }

        "return the correct maxLen for getMaxLen" in {
            val board = new Board(15, 24, 2, 1, "up", maxLen = 99)
            board.getMaxLen shouldBe 99
        }
    }
}