package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.{Board, Table, TokenStack}
import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.builderComponent.FieldDirectorInterface


class ThreePlayerFieldDirector(builder: PlayingFieldBuilderInterface) extends FieldDirectorInterface {
  override def construct(players: List[PlayerInterface]): PlayingFieldInterface = {
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