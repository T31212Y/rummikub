package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import de.htwg.se.rummikub.model.playerComponent.{PlayerInterface, PlayerFactoryInterface}
import de.htwg.se.rummikub.model.playingFieldComponent.{BoardInterface, PlayingFieldInterface, TokenStackFactoryInterface, TableFactoryInterface, BoardFactoryInterface}
import de.htwg.se.rummikub.model.gameModeComponent.GameModeTemplate
import de.htwg.se.rummikub.model.builderComponent.{PlayingFieldBuilderInterface, FieldDirectorInterface}
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.StandardTokenStructureFactory
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface

import com.google.inject.Inject
import com.google.inject.name.Named

case class TwoPlayerMode @Inject() (pns: List[String], 
                                        tokenStackFactory: TokenStackFactoryInterface, 
                                        tableFactory: TableFactoryInterface, 
                                        boardFactory: BoardFactoryInterface, 
                                        playerFactory: PlayerFactoryInterface,
                                        playingFieldBuilder: PlayingFieldBuilderInterface,
                                        @Named("TwoPlayer") director: FieldDirectorInterface) extends GameModeTemplate {

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
            if (field.getPlayers.size < 2) {
                println("Not enough players to update.")
                field
            } else {
                val player1 = field.getPlayers(0)
                val player2 = field.getPlayers(1)

                val boardP1 = field.getBoards(0)
                val boardP2 = field.getBoards(1)

                val updatedBoards = List(
                    updateBoardSinglePlayer(player1, boardP1).fold(boardP1)(identity),
                    updateBoardSinglePlayer(player2, boardP2).fold(boardP2)(identity)
                )

                val updatedInnerField = tableFactory.createTable(
                    cntRows - 4,
                    boardP1.size(boardP1.wrapBoardRowSingle(boardP1.getBoardELRP12_1)) - 2,
                    field.getInnerField.getTokensOnTable
                )

                field.updated(newPlayers = field.getPlayers, newBoards = updatedBoards, newInnerField = updatedInnerField)
            }
        }
    }

    override def renderPlayingField(playingField: Option[PlayingFieldInterface]): String = {
        playingField match {
            case Some(field) =>
                val player1 = field.getPlayers(0)
                val player2 = field.getPlayers(1)

                val boardP1 = field.getBoards(0)
                val boardP2 = field.getBoards(1)

                val innerField = field.getInnerField

                val edgeUp = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.getBoardELRP12_1)), player1.getName).getOrElse("") + "\n"
                val edgeDown = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.getBoardELRP12_1)), player2.getName).getOrElse("") + "\n"
            
                 s"$edgeUp${boardP1.toString()}${innerField.toString()}${boardP2.toString()}$edgeDown\n".replace("x", " ").replace("y", " ")    
            case None =>
                "No playing field available."
        }
    }

    override def updateBoardMultiPlayer(players: List[PlayerInterface], board: BoardInterface): Option[BoardInterface] = None

    override def lineWith2PlayerNames(char: String, length: Int, player1: String, player2: String): Option[String] = None
}