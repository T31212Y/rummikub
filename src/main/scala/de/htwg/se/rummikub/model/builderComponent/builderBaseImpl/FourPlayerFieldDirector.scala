package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.builderComponent.FieldDirectorInterface

import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableFactoryInterface

import de.htwg.se.rummikub.model.playingFieldComponent.BoardFactoryInterface

class FourPlayerFieldDirector(builder: PlayingFieldBuilderInterface, tokenStackFactory: TokenStackFactoryInterface, tableFactory: TableFactoryInterface, boardFactory: BoardFactoryInterface) extends FieldDirectorInterface {
  override def construct(players: List[PlayerInterface]): PlayingFieldInterface = {
    val boardP13 = boardFactory.createBoard(cntEdgeSpaces, cntTokens, 4, 2, "up", 116)
    val boardP24 = boardFactory.createBoard(cntEdgeSpaces, cntTokens, 4, 2, "down", 116)

    val innerField = tableFactory.createTable(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.getBoardELRP12_1, boardP13.getBoardELRP34_1)) - 2, List())
    val stack = tokenStackFactory.createShuffledStack

    builder.setPlayers(players)
      .setBoards(List(boardP13, boardP24))
      .setInnerField(innerField)
      .setStack(stack)
      .build()
  }
}