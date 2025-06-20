package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import scala.util.{Try, Failure, Success}

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.{BoardInterface, TableInterface, TokenStackInterface, PlayingFieldInterface}
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface

import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.PlayingField

class StandardPlayingFieldBuilder extends PlayingFieldBuilderInterface {
    private var players: Option[List[PlayerInterface]] = None
    private var boards: Option[List[BoardInterface]] = None
    private var innerField: Option[TableInterface] = None
    private var stack: Option[TokenStackInterface] = None

    override def setPlayers(players: List[PlayerInterface]): PlayingFieldBuilderInterface = {
      this.players = Some(players)
      this
    }

    override def setBoards(boards: List[BoardInterface]): PlayingFieldBuilderInterface = {
      this.boards = Some(boards)
      this
    }

    override def setInnerField(innerField: TableInterface): PlayingFieldBuilderInterface = {
      this.innerField = Some(innerField)
      this
    }

    override def setStack(stack: TokenStackInterface): PlayingFieldBuilderInterface = {
      this.stack = Some(stack)
      this
    }

    override def build(): PlayingFieldInterface = {
      val p = players.getOrElse(throw new IllegalStateException("Players not set"))
      val b = boards.getOrElse(throw new IllegalStateException("Boards not set"))
      val i = innerField.getOrElse(throw new IllegalStateException("InnerField not set"))
      val s = stack.getOrElse(throw new IllegalStateException("Stack not set"))
      PlayingField(p, b, i, s)
    }
}