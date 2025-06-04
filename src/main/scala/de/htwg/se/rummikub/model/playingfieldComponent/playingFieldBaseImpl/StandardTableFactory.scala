package de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingfieldComponent.{TableInterface, TableFactoryInterface}
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

class StandardTableFactory extends TableFactoryInterface {
    override def create(cntRows: Int, length: Int, tokensOnTable: List[List[TokenStructureInterface]] = List()): TableInterface = {
        new Table(cntRows, length, tokensOnTable)
    }
}