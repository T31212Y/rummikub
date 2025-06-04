package de.htwg.se.rummikub.model.gameModeComponent.gameMode2PlayerImpl

import de.htwg.se.rummikub.model.playerComponent.{PlayerInterface, PlayerFactoryInterface}
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface
import de.htwg.se.rummikub.model.gameModeComponent.GameModeInterface
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeTemplate
import de.htwg.se.rummikub.model.playingfieldComponent.{BoardInterface, PlayingFieldInterface, PlayingFieldFactoryInterface, TableFactoryInterface}
import de.htwg.se.rummikub.model.builderComponent.{BuilderFactoryInterface, FieldDirectorFactoryInterface}

case class TwoPlayerMode(override val playerNames: List[String],
override val playerFactory: PlayerFactoryInterface,
override val tokenStructureFactory: TokenStructureFactoryInterface,
builderFactory: BuilderFactoryInterface,
directorFactory: FieldDirectorFactoryInterface,
pfFactory: PlayingFieldFactoryInterface,
tableFactory: TableFactoryInterface
) extends GameModeInterface with GameModeTemplate {

    override def createPlayingField(players: List[PlayerInterface]): Option[PlayingFieldInterface] = {
        if (players.isEmpty) {
            println("Cannot create playing field: No players provided.")
            None
        } else {
            val builder = builderFactory.createBuilder(pfFactory)
            val director = directorFactory.create(2)

            Some(director.construct(players))
        }
    }

    override def updatePlayingField(playingField: Option[PlayingFieldInterface]): Option[PlayingFieldInterface] = {
        playingField.map { field =>
            val players = field.getPlayers
            val boards = field.getBoards
            val tokensOnTable = field.getInnerField.getTokens

            if (players.size < 2) {
                println("Not enough players to update.")
                field
            } else {
                val player1 = players(0)
                val player2 = players(1)

                val boardP1 = boards(0)
                val boardP2 = boards(1)

                val updatedBoards = List(
                    updateBoardSinglePlayer(player1, boardP1).fold(boardP1)(identity),
                    updateBoardSinglePlayer(player2, boardP2).fold(boardP2)(identity)
                )

                val updatedInnerField = tableFactory.create(
                    cntRows - 4,
                    boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)) - 2,
                    tokensOnTable
                )

                field.withUpdated(updatedBoards, updatedInnerField)
            }
        }
    }

    override def renderPlayingField(playingField: Option[PlayingFieldInterface]): String = {
        playingField match {
            case Some(field) =>
                val players = field.getPlayers
                val boards = field.getBoards

                val player1 = players(0)
                val player2 = players(1)

                val boardP1 = boards(0)
                val boardP2 = boards(1)

                val innerField = field.getInnerField

                val edgeUp = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player1.name).getOrElse("") + "\n"
                val edgeDown = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player2.name).getOrElse("") + "\n"
            
                 s"$edgeUp${boardP1.toString()}${innerField.toString()}${boardP2.toString()}$edgeDown\n".replace("x", " ").replace("y", " ")    
            case None =>
                "No playing field available."
        }
    }

    override def updateBoardMultiPlayer(players: List[PlayerInterface], board: BoardInterface): Option[BoardInterface] = None

    override def lineWith2PlayerNames(char: String, length: Int, player1: String, player2: String): Option[String] = None
}