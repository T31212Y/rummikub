package de.htwg.se.rummikub.model.playingFieldComponent

trait TableFactoryInterface {
    def createTable(cntRows: Int, length: Int): TableInterface
}