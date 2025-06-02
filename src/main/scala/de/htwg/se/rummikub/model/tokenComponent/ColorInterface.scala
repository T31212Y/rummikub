package de.htwg.se.rummikub.model.tokenComponent

trait ColorInterface {
    override def toString: String
    def reset: String
    def name: String
}