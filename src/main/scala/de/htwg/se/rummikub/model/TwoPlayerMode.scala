package de.htwg.se.rummikub.model
import playingfieldComponent.PlayingField
import playingfieldComponent.playingFieldBaseImpl.Table
import builderComponent.StandardPlayingFieldBuilder

case class TwoPlayerMode(playerNames: List[String]) extends GameModeTemplate(playerNames) {

    override def createPlayingField(players: List[Player]): Option[PlayingField] = {
        if (players.isEmpty) {
            println("Cannot create playing field: No players provided.")
            None
        } else {
            val builder = new StandardPlayingFieldBuilder
            val director = new TwoPlayerFieldDirector(builder)

            Some(director.construct(players))
        }
    }

    override def updatePlayingField(playingField: Option[PlayingField]): Option[PlayingField] = {
        playingField.map { field =>
            if (field.players.size < 2) {
                println("Not enough players to update.")
                field
            } else {
                val player1 = field.players(0)
                val player2 = field.players(1)

                val boardP1 = field.boards(0)
                val boardP2 = field.boards(1)

                val updatedBoards = List(
                    updateBoardSinglePlayer(player1, boardP1).fold(boardP1)(identity),
                    updateBoardSinglePlayer(player2, boardP2).fold(boardP2)(identity)
                )

                val updatedInnerField = new Table(
                    cntRows - 4,
                    boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)) - 2,
                    field.innerField.tokensOnTable
                )

                field.copy(boards = updatedBoards, innerField = updatedInnerField)
            }
        }
    }

    override def renderPlayingField(playingField: Option[PlayingField]): String = {
        playingField match {
            case Some(field) =>
                val player1 = field.players(0)
                val player2 = field.players(1)

                val boardP1 = field.boards(0)
                val boardP2 = field.boards(1)

                val innerField = field.innerField

                val edgeUp = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player1.name).getOrElse("") + "\n"
                val edgeDown = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player2.name).getOrElse("") + "\n"
            
                 s"$edgeUp${boardP1.toString()}${innerField.toString()}${boardP2.toString()}$edgeDown\n".replace("x", " ").replace("y", " ")    
            case None =>
                "No playing field available."
        }
    }

    override def updateBoardMultiPlayer(players: List[Player], board: Board): Option[Board] = None

    override def lineWith2PlayerNames(char: String, length: Int, player1: String, player2: String): Option[String] = None
}