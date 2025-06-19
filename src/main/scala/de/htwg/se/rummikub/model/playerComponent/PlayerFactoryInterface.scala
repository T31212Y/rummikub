package de.htwg.se.rummikub.model.playerComponent

trait PlayerFactoryInterface {
    def createPlayer(name: String): PlayerInterface
}