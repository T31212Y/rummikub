package de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

case class Board(cntEdgeSpaces: Int, amtTokens: Int, amtPlayers: Int, amtBoardsInRow: Int, dest: String, maxLen: Int = 90) {

    var boardELRP12_1 = "| " + ("x " * (amtTokens - 1)) + "|"
    var boardELRP12_2 = "| " + ("x " * (amtTokens - 1)) + "|"
    var boardELRP34_1 = "| " + ("y " * (amtTokens - 1)) + "|"
    var boardELRP34_2 = "| " + ("y " * (amtTokens - 1)) + "|"
    var boardEUD = "+" + ("-" * (2 * amtTokens - 1)) + "+"

    def size(board: String): Int = {
        deleteColorCodes(board).length
    }

    def formatBoardRow(row: List[TokenStructureInterface]): String = {
        val paddedRow = row.padTo(amtTokens, "")
        val formattedNumbers = paddedRow.map {
        case t: TokenStructureInterface => t.toString()
        case _           => "  "
        }.mkString(" ")

        s"| $formattedNumbers |"
    }

    def formatEmptyBoardRow(length: Int): String = {
        val emptyRow = " " * length
        s"| $emptyRow |"
    }

    def wrapBoardRowSingle(board: String): String = {
        val left = "|" + (" " * cntEdgeSpaces)
        val right = (" " * cntEdgeSpaces) + "|"
        s"$left$board$right"
    }

    def wrapBoardRowDouble(board1: String, board2: String): String = {
        val left = "|" + (" " * cntEdgeSpaces)
        val middle = (" " * cntEdgeSpaces)
        val right = (" " * cntEdgeSpaces) + "|"
        s"$left$board1$middle$board2$right"
    }

    def wrapBoardRowSingleToDouble(board: String, length: Int): String = {
        val len = (length + 4) / 2
        val left = "|" + (" " * len)
        val right = (" " * len ) + "|"
        s"$left$board$right"
    }

    def createBoardFrameSingle(row: List[TokenStructureInterface]): String = {
        val board = deleteColorCodes(formatBoardRow(row))

        val count = board.length - 2

        val frame = "+" + ("-" * count) + "+"
        wrapBoardRowSingle(frame)
    }

    def createBoardFrameDouble(row1: List[TokenStructureInterface], row2: List[TokenStructureInterface]): String = {
        val board1 = deleteColorCodes(formatBoardRow(row1))
        val board2 = deleteColorCodes(formatBoardRow(row2))

        val cntBoard1 = board1.length - 2
        val cntBoard2 = board2.length - 2

        val frameBoard1 = "+" + ("-" * cntBoard1) + "+"
        val frameBoard2 = "+" + ("-" * cntBoard2) + "+"

        wrapBoardRowDouble(frameBoard1, frameBoard2)
    }

    def createBoardFrameFromStringSingle(length: Int): String = {
        val frame = "+" + ("-" * length) + "+"
        wrapBoardRowSingle(frame)
    }

    def createBoardFrameFromStringDouble(length1: Int, length2: Int): String = {
        val frame1 = "+" + ("-" * length1) + "+"
        val frame2 = "+" + ("-" * length2) + "+"
        wrapBoardRowDouble(frame1, frame2)
    }

    def createBoardFrameFromStringSingleToDouble(length: Int): String = {
        val frame = "+" + ("-" * length) + "+"
        wrapBoardRowSingleToDouble(frame, maxLen)
    }

    def deleteColorCodes(board: String): String = {
        board.replaceAll("\u001B\\[[;\\d]*m", "")
    }

    override def toString(): String = {
        var board = ""
        if (amtBoardsInRow == 1 && dest.equals("up")) {
            board = wrapBoardRowSingle(boardELRP12_1) + "\n" + createBoardFrameFromStringSingle(size(boardELRP12_1) - 2) + "\n" + wrapBoardRowSingle(boardELRP12_2) + "\n" + createBoardFrameFromStringSingle(size(boardELRP12_2) - 2) + "\n"
        } else if (amtBoardsInRow == 2 && dest.equals("up")) {
            board = wrapBoardRowDouble(boardELRP12_1, boardELRP34_1) + "\n" + createBoardFrameFromStringDouble(size(boardELRP12_1) - 2, size(boardELRP34_1) - 2) + "\n" + wrapBoardRowDouble(boardELRP12_2, boardELRP34_2) + "\n" + createBoardFrameFromStringDouble(size(boardELRP12_2) - 2, size(boardELRP34_2) - 2) + "\n"
        } else if (amtBoardsInRow == 1 && dest.equals("down") && amtPlayers != 3) {
            board = createBoardFrameFromStringSingle(size(boardELRP12_1) - 2) + "\n" + wrapBoardRowSingle(boardELRP12_1) + "\n" + createBoardFrameFromStringSingle(size(boardELRP12_2) - 2) + "\n" + wrapBoardRowSingle(boardELRP12_2) + "\n"
        } else if (amtBoardsInRow == 1 && dest.equals("down") && amtPlayers == 3) {
            board = createBoardFrameFromStringSingleToDouble(size(boardELRP12_1) - 2) + "\n" + wrapBoardRowSingleToDouble(boardELRP12_1, maxLen) + "\n" + createBoardFrameFromStringSingleToDouble(size(boardELRP12_2) - 2) + "\n" + wrapBoardRowSingleToDouble(boardELRP12_2, maxLen) + "\n"
        }
        else if (amtBoardsInRow == 2 && dest.equals("down")) {
            board = createBoardFrameFromStringDouble(size(boardELRP12_1) - 2, size(boardELRP34_1) - 2) + "\n" + wrapBoardRowDouble(boardELRP12_1, boardELRP34_1) + "\n" + createBoardFrameFromStringDouble(size(boardELRP12_2) - 2, size(boardELRP34_2) - 2) + "\n" + wrapBoardRowDouble(boardELRP12_2, boardELRP34_2) + "\n"
        }
        board
    }
}