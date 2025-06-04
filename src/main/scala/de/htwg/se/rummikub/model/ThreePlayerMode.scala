package de.htwg.se.rummikub.model

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

import de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl.{Table, TokenStack, PlayingField}
import de.htwg.se.rummikub.model.playingFieldComponent.BoardInterface

case class ThreePlayerMode(playerNames: List[String]) extends GameModeTemplate(playerNames) {

    override def createPlayingField(players: List[PlayerInterface]): Option[PlayingField] = {
        if (players.isEmpty) {
            println("Cannot create playing field: No players provided.")
            None
        } else {
            val builder = new StandardPlayingFieldBuilder
            val director = new ThreePlayerFieldDirector(builder)

            Some(director.construct(players))
        }
    }

    override def updatePlayingField(playingField: Option[PlayingField]): Option[PlayingField] = {
        playingField.map { field =>
            if (field.players.size < 3) {
                println("Not enough players to update.")
                field
            } else {
                val player1 = field.players(0)
                val player2 = field.players(1)
                val player3 = field.players(2)

                val boardP13 = field.boards(0)
                val boardP2 = field.boards(1)

                val updatedBoardP13 = updateBoardMultiPlayer(List(player1, player3), boardP13).fold(boardP13)(identity)
                val updatedBoardP2 = updateBoardSinglePlayer(player2, boardP2.updated(newMaxLen = 116)).fold(boardP2)(identity)

                val updatedBoards = List(
                    updatedBoardP13,
                    updatedBoardP2
                )

                val updatedInnerField = new Table(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.getBoardELRP12_1, boardP13.getBoardELRP34_1)) - 2, field.innerField.tokensOnTable)

                field.copy(boards = updatedBoards, innerField = updatedInnerField)
            }
        }
        
    }

    override def renderPlayingField(playingField: Option[PlayingField]): String = {
        playingField match {
            case Some(field) =>
                val player1 = field.players(0)
                val player2 = field.players(1)
                val player3 = field.players(2)

                val boardP13 = field.boards(0)
                val boardP2 = field.boards(1)

                val innerField = field.innerField

                val edgeUp = lineWith2PlayerNames("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.getBoardELRP12_1, boardP13.getBoardELRP34_1)), player1.getName, player3.getName).getOrElse("") + "\n"
                val edgeDown = lineWithPlayerName("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.getBoardELRP12_1, boardP13.getBoardELRP34_1)), player2.getName).getOrElse("") + "\n"

                s"$edgeUp${boardP13.toString()}${innerField.toString()}${boardP2.toString()}$edgeDown\n".replace("x", " ").replace("y", " ")
            case None =>
                "No playing field available."
        }
    }
}