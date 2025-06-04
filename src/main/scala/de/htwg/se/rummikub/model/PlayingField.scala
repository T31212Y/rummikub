package de.htwg.se.rummikub.model

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

case class PlayingField(players: List[PlayerInterface], boards: List[Board], innerField: Table, stack: TokenStack)