package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

import de.htwg.se.rummikub.model.playingFieldComponent.BoardInterface
import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface

import de.htwg.se.rummikub.model.gameModeComponent.GameModeTemplate
import de.htwg.se.rummikub.model.builderComponent.builderBaseImpl.StandardPlayingFieldBuilder
import de.htwg.se.rummikub.model.builderComponent.builderBaseImpl.ThreePlayerFieldDirector

import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableFactoryInterface
import de.htwg.se.rummikub.model.playingFieldComponent.BoardFactoryInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerFactoryInterface

import com.google.inject.Inject
import com.google.inject.name.Named
import de.htwg.se.rummikub.model.builderComponent.FieldDirectorInterface
import de.htwg.se.rummikub.model.builderComponent.PlayingFieldBuilderInterface

case class ThreePlayerMode @Inject() (pns: List[String], 
                                        tokenStackFactory: TokenStackFactoryInterface, 
                                        tableFactory: TableFactoryInterface, 
                                        boardFactory: BoardFactoryInterface, 
                                        playerFactory: PlayerFactoryInterface,
                                        playingFieldBuilder: PlayingFieldBuilderInterface,
                                        @Named("ThreePlayer") director: FieldDirectorInterface) extends GameModeTemplate {

    val playerNames: List[String] = pns

    override def createPlayingField(players: List[PlayerInterface]): Option[PlayingFieldInterface] = {
        if (players.isEmpty) {
            println("Cannot create playing field: No players provided.")
            None
        } else {
            Some(director.construct(players))
        }
    }

    override def createPlayers: List[PlayerInterface] = {
        playerNames.map(name => playerFactory.createPlayer(name))
    }

    override def updatePlayingField(playingField: Option[PlayingFieldInterface]): Option[PlayingFieldInterface] = {
        playingField.map { field =>
            if (field.getPlayers.size < 3) {
                println("Not enough players to update.")
                field
            } else {
                val player1 = field.getPlayers(0)
                val player2 = field.getPlayers(1)
                val player3 = field.getPlayers(2)

                val boardP13 = field.getBoards(0)
                val boardP2 = field.getBoards(1)

                val updatedBoardP13 = updateBoardMultiPlayer(List(player1, player3), boardP13).fold(boardP13)(identity)
                val updatedBoardP2 = updateBoardSinglePlayer(player2, boardP2.updated(newMaxLen = 116)).fold(boardP2)(identity)

                val updatedBoards = List(
                    updatedBoardP13,
                    updatedBoardP2
                )

                val updatedInnerField = tableFactory.createTable(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.getBoardELRP12_1, boardP13.getBoardELRP34_1)) - 2, field.getInnerField.getTokensOnTable)

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

                val boardP13 = field.getBoards(0)
                val boardP2 = field.getBoards(1)

                val innerField = field.getInnerField

                val edgeUp = lineWith2PlayerNames("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.getBoardELRP12_1, boardP13.getBoardELRP34_1)), player1.getName, player3.getName).getOrElse("") + "\n"
                val edgeDown = lineWithPlayerName("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.getBoardELRP12_1, boardP13.getBoardELRP34_1)), player2.getName).getOrElse("") + "\n"

                s"$edgeUp${boardP13.toString()}${innerField.toString()}${boardP2.toString()}$edgeDown\n".replace("x", " ").replace("y", " ")
            case None =>
                "No playing field available."
        }
    }
}