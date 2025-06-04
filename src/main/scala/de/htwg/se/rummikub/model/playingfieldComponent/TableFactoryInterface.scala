package de.htwg.se.rummikub.model.playingfieldComponent

import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

trait TableFactoryInterface {
  def create(cntRows: Int, length: Int, tokensOnTable: List[List[TokenStructureInterface]] = List()): TableInterface
}