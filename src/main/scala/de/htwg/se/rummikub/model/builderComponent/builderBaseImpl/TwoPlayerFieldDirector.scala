package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.{PlayingFieldInterface, TokenStackFactoryInterface, TableFactoryInterface, BoardFactoryInterface}
import de.htwg.se.rummikub.model.builderComponent.{PlayingFieldBuilderInterface, FieldDirectorInterface}

import com.google.inject.Inject

class TwoPlayerFieldDirector @Inject() (builder: PlayingFieldBuilderInterface, tokenStackFactory: TokenStackFactoryInterface, tableFactory: TableFactoryInterface, boardFactory: BoardFactoryInterface) extends FieldDirectorInterface {
  override def construct(players: List[PlayerInterface]): PlayingFieldInterface = {
    val boardP1 = boardFactory.createBoard(cntEdgeSpaces, cntTokens, 2, 1, "up", 90)
    val boardP2 = boardFactory.createBoard(cntEdgeSpaces, cntTokens, 2, 1, "down", 90)

    val innerField = tableFactory.createTable(cntRows - 4, boardP1.size(boardP1.wrapBoardRowSingle(boardP1.getBoardELRP12_1)) - 2, List())
    val stack = tokenStackFactory.createShuffledStack

    builder.setPlayers(players)
      .setBoards(List(boardP1, boardP2))
      .setInnerField(innerField)
      .setStack(stack)
      .build()
  }
}