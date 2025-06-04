package de.htwg.se.rummikub.model.builderComponent

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.PlayingFieldInterface

trait FieldDirectorInterface {
    val cntRows: Int = 20
    val cntTokens: Int = 24
    val cntEdgeSpaces: Int = 15

    def construct(players: List[PlayerInterface]): PlayingFieldInterface
}