package de.htwg.se.rummikub.model

class TwoPlayerFieldDirector(builder: PlayingFieldBuilder) {

    val cntRows: Int = 20
    val cntTokens: Int = 24
    val cntEdgeSpaces: Int = 15

    def construct(players: List[Player]): PlayingField = {
      val boardP1 = new Board(cntEdgeSpaces, cntTokens, 2, 1, "up")
      val boardP2 = new Board(cntEdgeSpaces, cntTokens, 2, 1, "down")

      val innerField = new Table(cntRows - 4, boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)) - 2)
  
      builder.setPlayers(players)
        .setBoards(List(boardP1, boardP2))
        .setInnerField(innerField)
        .build()
    }
}