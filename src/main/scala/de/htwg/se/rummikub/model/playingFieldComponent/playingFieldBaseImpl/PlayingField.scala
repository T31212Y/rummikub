package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingFieldComponent.BoardInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableInterface

case class PlayingField(players: List[PlayerInterface], boards: List[BoardInterface], innerField: TableInterface, stack: TokenStack)