package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingFieldComponent.{TableFactoryInterface, TableInterface}
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

class StandardTableFactory extends TableFactoryInterface {
  override def createTable(cntRows: Int, length: Int, tokensOnTable: List[List[TokenInterface]]): TableInterface = new Table(cntRows, length, tokensOnTable)
}