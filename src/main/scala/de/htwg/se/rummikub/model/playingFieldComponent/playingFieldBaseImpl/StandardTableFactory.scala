package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingFieldComponent.{TableFactoryInterface, TableInterface}

class StandardTableFactory extends TableFactoryInterface {
  override def createTable(cntRows: Int, length: Int): TableInterface = new Table(cntRows, length)
}