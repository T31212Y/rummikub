package de.htwg.se.rummikub.model

import scala.util.{Try, Failure, Success}
import playingfieldComponent.PlayingField
import playingfieldComponent.playingFieldBaseImpl.Table

class StandardPlayingFieldBuilder extends PlayingFieldBuilder {
    private var players: Option[List[Player]] = None
    private var boards: Option[List[Board]] = None
    private var innerField: Option[Table] = None
    private var stack: Option[TokenStack] = None

    override def setPlayers(players: List[Player]): PlayingFieldBuilder = {
      this.players = Some(players)
      this
    }

    override def setBoards(boards: List[Board]): PlayingFieldBuilder = {
      this.boards = Some(boards)
      this
    }

    override def setInnerField(innerField: Table): PlayingFieldBuilder = {
      this.innerField = Some(innerField)
      this
    }

    override def setStack(stack: TokenStack): PlayingFieldBuilder = {
      this.stack = Some(stack)
      this
    }

    override def build(): PlayingField = {
      val p = players.getOrElse(throw new IllegalStateException("Players not set"))
      val b = boards.getOrElse(throw new IllegalStateException("Boards not set"))
      val i = innerField.getOrElse(throw new IllegalStateException("InnerField not set"))
      val s = stack.getOrElse(throw new IllegalStateException("Stack not set"))
      PlayingField(p, b, i, s)
    }
}