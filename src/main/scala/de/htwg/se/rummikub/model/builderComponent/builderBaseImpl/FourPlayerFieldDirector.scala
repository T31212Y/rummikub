package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import de.htwg.se.rummikub.model.playingfieldComponent.{PlayingFieldInterface, BoardFactoryInterface, TableFactoryInterface, TokenStackFactoryInterface}
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.builderComponent.FieldDirectorInterface

class FourPlayerFieldDirector(
  builder: PlayingFieldBuilderInterface, 
  boardFactory: BoardFactoryInterface,
  tableFactory: TableFactoryInterface,
  stackFactory: TokenStackFactoryInterface
  ) extends FieldDirectorInterface {
  
  val cntRows: Int = 20
  val cntTokens: Int = 24
  val cntEdgeSpaces: Int = 15

  override def construct(players: List[PlayerInterface]): PlayingFieldInterface = {
    val boardP13 = boardFactory.create(cntEdgeSpaces, cntTokens, 4, 2, "up")
    val boardP24 = boardFactory.create(cntEdgeSpaces, cntTokens, 4, 2, "down")

    val innerField = tableFactory.create(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)) - 2)
    val stack = stackFactory.create

    builder.setPlayers(players)
      .setBoards(List(boardP13, boardP24))
      .setInnerField(innerField)
      .setStack(stack)
      .build()
  }
}