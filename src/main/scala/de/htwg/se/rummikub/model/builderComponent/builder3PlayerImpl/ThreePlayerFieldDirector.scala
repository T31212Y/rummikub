package de.htwg.se.rummikub.model.builderComponent.builder3PlayerImpl

import de.htwg.se.rummikub.model.playingfieldComponent.{PlayingFieldInterface, BoardFactoryInterface, TableFactoryInterface, TokenStackFactoryInterface}
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

class ThreePlayerFieldDirector(
  builder: PlayingFieldBuilderInterface, 
  boardFactory: BoardFactoryInterface,
  tableFactory: TableFactoryInterface,
  stackFactory: TokenStackFactoryInterface
  ) {
  
  val cntRows: Int = 20
  val cntTokens: Int = 24
  val cntEdgeSpaces: Int = 15

  def construct(players: List[PlayerInterface]): PlayingFieldInterface = {
    val boardP13 = boardFactory.create(cntEdgeSpaces, cntTokens, 3, 2, "up")
    val boardP2 = boardFactory.create(cntEdgeSpaces, cntTokens, 3, 1, "down")

    val innerField = tableFactory.create(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)) - 2)
    val stack = stackFactory.create

    builder.setPlayers(players)
      .setBoards(List(boardP13, boardP2))
      .setInnerField(innerField)
      .setStack(stack)
      .build()
  }
}