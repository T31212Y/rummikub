package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingFieldComponent.{BoardFactoryInterface, BoardInterface}

class StandardBoardFactory extends BoardFactoryInterface {
    override def createBoard(cntEdgeSpaces: Int, amtTokens: Int, amtPlayers: Int, amtBoardsInRow: Int, dest: String, maxLen: Int): BoardInterface = new Board(cntEdgeSpaces, amtTokens, amtPlayers, amtBoardsInRow, dest, maxLen)
}