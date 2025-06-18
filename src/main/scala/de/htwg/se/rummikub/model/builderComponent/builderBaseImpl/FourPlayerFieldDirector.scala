package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.{Board, Table}
import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.builderComponent.FieldDirectorInterface

import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface

class FourPlayerFieldDirector(builder: PlayingFieldBuilderInterface, tokenStackFactory: TokenStackFactoryInterface) extends FieldDirectorInterface {
  override def construct(players: List[PlayerInterface]): PlayingFieldInterface = {
    val boardP13 = new Board(cntEdgeSpaces, cntTokens, 4, 2, "up")
    val boardP24 = new Board(cntEdgeSpaces, cntTokens, 4, 2, "down")

    val innerField = new Table(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)) - 2)
    val stack = tokenStackFactory.createShuffledStack

    builder.setPlayers(players)
      .setBoards(List(boardP13, boardP24))
      .setInnerField(innerField)
      .setStack(stack)
      .build()
  }
}