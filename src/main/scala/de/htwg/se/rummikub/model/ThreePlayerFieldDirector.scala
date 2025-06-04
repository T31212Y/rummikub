package de.htwg.se.rummikub.model

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

class ThreePlayerFieldDirector(builder: PlayingFieldBuilder) {
  
  val cntRows: Int = 20
  val cntTokens: Int = 24
  val cntEdgeSpaces: Int = 15

  def construct(players: List[PlayerInterface]): PlayingField = {
    val boardP13 = new Board(cntEdgeSpaces, cntTokens, 3, 2, "up")
    val boardP2 = new Board(cntEdgeSpaces, cntTokens, 3, 1, "down")

    val innerField = new Table(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)) - 2)
    val stack = TokenStack.apply()

    builder.setPlayers(players)
      .setBoards(List(boardP13, boardP2))
      .setInnerField(innerField)
      .setStack(stack)
      .build()
  }
}