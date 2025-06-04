package de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingfieldComponent.BoardInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

case class Board(cntEdgeSpaces: Int, amtTokens: Int, amtPlayers: Int, amtBoardsInRow: Int, dest: String, maxLen: Int = 90) extends BoardInterface {

  var boardELRP12_1 = "| " + ("x " * (amtTokens - 1)) + "|"
  var boardELRP12_2 = "| " + ("x " * (amtTokens - 1)) + "|"
  var boardELRP34_1 = "| " + ("y " * (amtTokens - 1)) + "|"
  var boardELRP34_2 = "| " + ("y " * (amtTokens - 1)) + "|"
  var boardEUD = "+" + ("-" * (2 * amtTokens - 1)) + "+"

  def size(board: String): Int = {
    deleteColorCodes(board).length
  }

  def deleteColorCodes(board: String): String = {
    board.replaceAll("\u001B\\[[;\\d]*m", "")
  }

  override def toStringRepresentation: String = {
    var board = ""
    if (amtBoardsInRow == 1 && dest.equals("up")) {
      board = wrapBoardRowSingle(boardELRP12_1) + "\n" + createBoardFrameFromStringSingle(size(boardELRP12_1) - 2) + "\n" +
        wrapBoardRowSingle(boardELRP12_2) + "\n" + createBoardFrameFromStringSingle(size(boardELRP12_2) - 2) + "\n"
    } else if (amtBoardsInRow == 2 && dest.equals("up")) {
      board = wrapBoardRowDouble(boardELRP12_1, boardELRP34_1) + "\n" + createBoardFrameFromStringDouble(size(boardELRP12_1) - 2, size(boardELRP34_1) - 2) + "\n" +
        wrapBoardRowDouble(boardELRP12_2, boardELRP34_2) + "\n" + createBoardFrameFromStringDouble(size(boardELRP12_2) - 2, size(boardELRP34_2) - 2) + "\n"
    } else if (amtBoardsInRow == 1 && dest.equals("down") && amtPlayers != 3) {
      board = createBoardFrameFromStringSingle(size(boardELRP12_1) - 2) + "\n" + wrapBoardRowSingle(boardELRP12_1) + "\n" +
        createBoardFrameFromStringSingle(size(boardELRP12_2) - 2) + "\n" + wrapBoardRowSingle(boardELRP12_2) + "\n"
    } else if (amtBoardsInRow == 1 && dest.equals("down") && amtPlayers == 3) {
      board = createBoardFrameFromStringSingleToDouble(size(boardELRP12_1) - 2) + "\n" + wrapBoardRowSingleToDouble(boardELRP12_1, maxLen) + "\n" +
        createBoardFrameFromStringSingleToDouble(size(boardELRP12_2) - 2) + "\n" + wrapBoardRowSingleToDouble(boardELRP12_2, maxLen) + "\n"
    } else if (amtBoardsInRow == 2 && dest.equals("down")) {
      board = createBoardFrameFromStringDouble(size(boardELRP12_1) - 2, size(boardELRP34_1) - 2) + "\n" +
        wrapBoardRowDouble(boardELRP12_1, boardELRP34_1) + "\n" + createBoardFrameFromStringDouble(size(boardELRP12_2) - 2, size(boardELRP34_2) - 2) + "\n" +
        wrapBoardRowDouble(boardELRP12_2, boardELRP34_2) + "\n"
    }
    board
  }

  def formatBoardRow(row: List[TokenStructureInterface]): String = {
    val paddedRow = row.padTo(amtTokens, "")
    val formattedNumbers = paddedRow.map {
      case t: TokenStructureInterface => t.toString()
      case _                         => "  "
    }.mkString(" ")
    s"| $formattedNumbers |"
  }

  def formatEmptyBoardRow(length: Int): String = {
    val emptyRow = " " * length
    s"| $emptyRow |"
  }

  override def wrapBoardRowSingle(board: String): String = {
    val left = "|" + (" " * cntEdgeSpaces)
    val right = (" " * cntEdgeSpaces) + "|"
    s"$left$board$right"
  }

  override def wrapBoardRowDouble(board1: String, board2: String): String = {
    val left = "|" + (" " * cntEdgeSpaces)
    val middle = (" " * cntEdgeSpaces)
    val right = (" " * cntEdgeSpaces) + "|"
    s"$left$board1$middle$board2$right"
  }


  def wrapBoardRowSingleToDouble(board: String, maxLen: Int): String = {
    val left = "|" + (" " * cntEdgeSpaces)
    val rightLen = maxLen - (cntEdgeSpaces + 1 + board.length + cntEdgeSpaces + 1)
    val right = (" " * rightLen) + "|"
    s"$left$board$right"
  }

  def createBoardFrameFromStringSingle(len: Int): String = {
    "+" + ("-" * len) + "+"
  }

  def createBoardFrameFromStringSingleToDouble(len: Int): String = {
    val left = "+" + ("-" * len) + "+"
    val right = ("-" * (maxLen - len - 2)) + "+"
    s"$left$right"
  }

  def createBoardFrameFromStringDouble(len1: Int, len2: Int): String = {
    val left = "+" + ("-" * len1) + "+"
    val right = ("-" * len2) + "+"
    s"$left$right"
  }

}