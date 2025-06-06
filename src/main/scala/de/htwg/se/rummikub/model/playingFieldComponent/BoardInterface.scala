package de.htwg.se.rummikub.model.playingFieldComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

trait BoardInterface {
    def getCntEdgeSpaces: Int
    def getAmtTokens: Int
    def getAmtPlayers: Int
    def getAmtBoardsInRow: Int
    def getDest: String
    def getMaxLen: Int

    def getBoardELRP12_1: String
    def getBoardELRP12_2: String
    def getBoardELRP34_1: String
    def getBoardELRP34_2: String
    def getBoardEUD: String

    def setBoardELRP12_1(board: String): Unit
    def setBoardELRP12_2(board: String): Unit
    def setBoardELRP34_1(board: String): Unit
    def setBoardELRP34_2(board: String): Unit
    def setBoardEUD(be: String): Unit

    def size(board: String): Int
    def formatBoardRow(row: List[TokenInterface]): String
    def formatEmptyBoardRow(length: Int): String
    def wrapBoardRowSingle(board: String): String
    def wrapBoardRowDouble(board1: String, board2: String): String
    def wrapBoardRowSingleToDouble(board: String, length: Int): String
    def createBoardFrameSingle(row: List[TokenInterface]): String
    def createBoardFrameDouble(row1: List[TokenInterface], row2: List[TokenInterface]): String
    def createBoardFrameFromStringSingle(length: Int): String
    def createBoardFrameFromStringDouble(length1: Int, length2: Int): String
    def createBoardFrameFromStringSingleToDouble(length: Int): String
    def deleteColorCodes(board: String): String

    def updated(newMaxLen: Int): BoardInterface

    override def toString(): String
}