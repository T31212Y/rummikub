package de.htwg.se.rummikub.model.playingfieldComponent

import de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl.{Board, Table, TokenStack}
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

case class PlayingFieldInterface(players: List[PlayerInterface], boards: List[Board], innerField: Table, stack: TokenStack)