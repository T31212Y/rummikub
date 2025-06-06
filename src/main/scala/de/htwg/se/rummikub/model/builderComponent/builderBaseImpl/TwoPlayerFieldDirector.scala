package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.{Board, Table, TokenStack}
import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.builderComponent.FieldDirectorInterface

class TwoPlayerFieldDirector(builder: PlayingFieldBuilderInterface) extends FieldDirectorInterface {
  override def construct(players: List[PlayerInterface]): PlayingFieldInterface = {
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