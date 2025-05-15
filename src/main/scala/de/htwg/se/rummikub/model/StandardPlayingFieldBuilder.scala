package de.htwg.se.rummikub.model

import scala.compiletime.uninitialized

class StandardPlayingFieldBuilder extends PlayingFieldBuilder {
    private var players: List[Player] = uninitialized
    private var boards: List[Board] = uninitialized
    private var innerField: Table = uninitialized
  
    override def setPlayers(players: List[Player]): PlayingFieldBuilder = {
      this.players = players
      this
    }
  
    override def setBoards(boards: List[Board]): PlayingFieldBuilder = {
      this.boards = boards
      this
    }
  
    override def setInnerField(innerField: Table): PlayingFieldBuilder = {
      this.innerField = innerField
      this
    }
  
    override def build(): PlayingField = {
      PlayingField(players, boards, innerField)
    }
}