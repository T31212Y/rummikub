package de.htwg.se.rummikub.model.playingFieldComponent

trait BoardFactoryInterface {
    def createBoard(cntEdgeSpaces: Int, amtTokens: Int, amtPlayers: Int, amtBoardsInRow: Int, dest: String, maxLen: Int): BoardInterface
}