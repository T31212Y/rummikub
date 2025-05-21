package de.htwg.se.rummikub.model

import scala.util.{Try, Failure, Success}

class StandardPlayingFieldBuilder extends PlayingFieldBuilder {
    private var players: Option[List[Player]] = None
    private var boards: Option[List[Board]] = None
    private var innerField: Option[Table] = None

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

    override def build(): Try[PlayingField] = for {
      p <- players.toRight(new IllegalStateException("Players not set")).toTry
      b <- boards.toRight(new IllegalStateException("Boards not set")).toTry
      i <- innerField.toRight(new IllegalStateException("InnerField not set")).toTry
    } yield PlayingField(p, b, i)
}