package de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingfieldComponent.{BoardInterface, BoardFactoryInterface}

class StandardBoardFactory extends BoardFactoryInterface {
  override def create(cntEdgeSpaces: Int, amtTokens: Int, amtPlayers: Int, amtBoardsInRow: Int, dest: String, maxLen: Int = 90): BoardInterface =
    new Board(cntEdgeSpaces, amtTokens, amtPlayers, amtBoardsInRow, dest, maxLen)
}