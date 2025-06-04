package de.htwg.se.rummikub.model.playingfieldComponent

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

trait PlayingFieldFactoryInterface {
    def create(players: List[PlayerInterface], boards: List[BoardInterface], innerField: TableInterface, stack: TokenStackInterface): PlayingFieldInterface
}