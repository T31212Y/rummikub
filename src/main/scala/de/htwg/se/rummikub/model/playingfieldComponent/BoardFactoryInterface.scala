package de.htwg.se.rummikub.model.playingfieldComponent

trait BoardFactoryInterface {
  def create(cntEdgeSpaces: Int, amtTokens: Int, amtPlayers: Int, amtBoardsInRow: Int, dest: String, maxLen: Int = 90): BoardInterface
}