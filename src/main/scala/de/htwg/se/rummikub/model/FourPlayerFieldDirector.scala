package de.htwg.se.rummikub.model
import playingfieldComponent.PlayingField
import playingfieldComponent.playingFieldBaseImpl.Table

class FourPlayerFieldDirector(builder: PlayingFieldBuilder) {
  
  val cntRows: Int = 20
  val cntTokens: Int = 24
  val cntEdgeSpaces: Int = 15

  def construct(players: List[Player]): PlayingField = {
    val boardP13 = new Board(cntEdgeSpaces, cntTokens, 4, 2, "up")
    val boardP24 = new Board(cntEdgeSpaces, cntTokens, 4, 2, "down")

    val innerField = new Table(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)) - 2)
    val stack = TokenStack.apply()

    builder.setPlayers(players)
      .setBoards(List(boardP13, boardP24))
      .setInnerField(innerField)
      .setStack(stack)
      .build()
  }
}