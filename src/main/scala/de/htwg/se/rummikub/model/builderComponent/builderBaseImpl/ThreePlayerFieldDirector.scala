package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface
import de.htwg.se.rummikub.model.builderComponent.FieldDirectorInterface

import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableFactoryInterface

import de.htwg.se.rummikub.model.playingFieldComponent.BoardFactoryInterface

import com.google.inject.Inject

class ThreePlayerFieldDirector @Inject() (builder: PlayingFieldBuilderInterface, tokenStackFactory: TokenStackFactoryInterface, tableFactory: TableFactoryInterface, boardFactory: BoardFactoryInterface) extends FieldDirectorInterface {
  override def construct(players: List[PlayerInterface]): PlayingFieldInterface = {
    val boardP13 = boardFactory.createBoard(cntEdgeSpaces, cntTokens, 3, 2, "up", 90)
    val boardP2 = boardFactory.createBoard(cntEdgeSpaces, cntTokens, 3, 1, "down", 90)

    val innerField = tableFactory.createTable(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.getBoardELRP12_1, boardP13.getBoardELRP34_1)) - 2, List())
    val stack = tokenStackFactory.createShuffledStack

    builder.setPlayers(players)
      .setBoards(List(boardP13, boardP2))
      .setInnerField(innerField)
      .setStack(stack)
      .build()
  }
}