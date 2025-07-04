package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import de.htwg.se.rummikub.model.playerComponent.{PlayerInterface, PlayerFactoryInterface}
import de.htwg.se.rummikub.model.playingFieldComponent.{BoardInterface, PlayingFieldInterface, TokenStackFactoryInterface, TableFactoryInterface, BoardFactoryInterface}
import de.htwg.se.rummikub.model.gameModeComponent.GameModeTemplate
import de.htwg.se.rummikub.model.builderComponent.{PlayingFieldBuilderInterface, FieldDirectorInterface}
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.StandardTokenStructureFactory
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface

import com.google.inject.Inject
import com.google.inject.name.Named

case class FourPlayerMode @Inject() (pns: List[String], tokenStackFactory: TokenStackFactoryInterface,
                                      tableFactory: TableFactoryInterface, boardFactory: BoardFactoryInterface,
                                      playerFactory: PlayerFactoryInterface, playingFieldBuilder: PlayingFieldBuilderInterface,
                                      @Named("FourPlayer") director: FieldDirectorInterface) extends GameModeTemplate {

    val playerNames: List[String] = pns

    override def createPlayingField(players: List[PlayerInterface]): Option[PlayingFieldInterface] = {
        if (players.isEmpty) {
            println("Cannot create playing field: No players provided.")
            None
        } else {
            Some(director.construct(players))
        }
    }

    override val tokenStructureFactory: TokenStructureFactoryInterface = new StandardTokenStructureFactory

    override def createPlayers: List[PlayerInterface] = {
        playerNames.map(name => playerFactory.createPlayer(name))
    }

    override def updatePlayingField(playingField: Option[PlayingFieldInterface]): Option[PlayingFieldInterface] = {
        playingField.map { field =>
            if (field.getPlayers.size < 4) {
                println("Not enough players to update.")
                field
            } else {
                val player1 = field.getPlayers(0)
                val player2 = field.getPlayers(1)
                val player3 = field.getPlayers(2)
                val player4 = field.getPlayers(3)

                val boardP13 = field.getBoards(0)
                val boardP24 = field.getBoards(1)

                val updatedBoards = List(
                    updateBoardMultiPlayer(List(player1, player3), boardP13).fold(boardP13)(identity),
                    updateBoardMultiPlayer(List(player2, player4), boardP24).fold(boardP24)(identity)
                )

                val updatedInnerField = tableFactory.createTable(cntRows - 4, boardP24.size(boardP24.wrapBoardRowDouble(boardP24.getBoardELRP12_1, boardP24.getBoardELRP34_1)) - 2, field.getInnerField.getTokensOnTable)

                field.updated(newPlayers = field.getPlayers, newBoards = updatedBoards, newInnerField = updatedInnerField)
            }
        }
    }

    override def renderPlayingField(playingField: Option[PlayingFieldInterface]): String = {
        playingField match {
            case Some(field) =>
                val player1 = field.getPlayers(0)
                val player2 = field.getPlayers(1)
                val player3 = field.getPlayers(2)
                val player4 = field.getPlayers(3)

                val boardP13 = field.getBoards(0)
                val boardP24 = field.getBoards(1)

                val innerField = field.getInnerField

                val edgeUp = lineWith2PlayerNames("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.getBoardELRP12_1, boardP13.getBoardELRP34_1)), player1.getName, player3.getName).getOrElse("") + "\n"
                val edgeDown = lineWith2PlayerNames("*", boardP24.size(boardP24.wrapBoardRowDouble(boardP24.getBoardELRP12_1, boardP24.getBoardELRP34_1)), player2.getName, player4.getName).getOrElse("") + "\n"

                s"$edgeUp${boardP13.toString()}${innerField.toString()}${boardP24.toString()}$edgeDown\n".replace("x", " ").replace("y", " ")
            case None =>
                "No playing field available."
        }
    }

    override def updateBoardSinglePlayer(player: PlayerInterface, board: BoardInterface): Option[BoardInterface] = None

    override def lineWithPlayerName(char: String, length: Int, player: String): Option[String] = None
}