package de.htwg.se.rummikub.model.builderComponent.builder2PlayerImpl

import de.htwg.se.rummikub.model.playingfieldComponent.PlayingFieldInterface
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface

class TwoPlayerFieldDirector(builder: PlayingFieldBuilderInterface) {

    val cntRows: Int = 20
    val cntTokens: Int = 24
    val cntEdgeSpaces: Int = 15

    def construct(players: List[Player]): PlayingFieldInterface = {
      val boardP1 = new Board(cntEdgeSpaces, cntTokens, 2, 1, "up")
      val boardP2 = new Board(cntEdgeSpaces, cntTokens, 2, 1, "down")

      val innerField = new Table(cntRows - 4, boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)) - 2)
      val stack = TokenStack.apply()

      builder.setPlayers(players)
        .setBoards(List(boardP1, boardP2))
        .setInnerField(innerField)
        .setStack(stack)
        .build()
    }
}