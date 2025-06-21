package de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.{TokenStructureFactoryInterface, TokenStructureInterface}

class StandardTokenStructureFactory extends TokenStructureFactoryInterface {

  override def createGroup(tokens: List[TokenInterface]): TokenStructureInterface = new Group(tokens)

  override def createRow(tokens: List[TokenInterface]): TokenStructureInterface = new Row(tokens)

  //override def tokenStructureFactory: TokenStructureFactoryInterface = this
}