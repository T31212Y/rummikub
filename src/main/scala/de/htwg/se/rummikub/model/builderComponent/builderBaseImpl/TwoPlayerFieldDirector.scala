package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import de.htwg.se.rummikub.model.playingfieldComponent.{PlayingFieldInterface, BoardFactoryInterface, TableFactoryInterface, TokenStackFactoryInterface}
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.builderComponent.FieldDirectorInterface

class TwoPlayerFieldDirector(
  builder: PlayingFieldBuilderInterface, 
  boardFactory: BoardFactoryInterface,
  tableFactory: TableFactoryInterface,
  stackFactory: TokenStackFactoryInterface
  ) extends FieldDirectorInterface {

    val cntRows: Int = 20
    val cntTokens: Int = 24
    val cntEdgeSpaces: Int = 15

    override def construct(players: List[PlayerInterface]): PlayingFieldInterface = {
      val boardP1 = boardFactory.create(cntEdgeSpaces, cntTokens, 2, 1, "up")
      val boardP2 = boardFactory.create(cntEdgeSpaces, cntTokens, 2, 1, "down")

      val innerField = tableFactory.create(cntRows - 4, boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)) - 2)
      val stack = stackFactory.create

      builder.setPlayers(players)
        .setBoards(List(boardP1, boardP2))
        .setInnerField(innerField)
        .setStack(stack)
        .build()
    }
}