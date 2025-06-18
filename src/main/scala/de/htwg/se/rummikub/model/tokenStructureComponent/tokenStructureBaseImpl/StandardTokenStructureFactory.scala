package de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.{TokenStructureFactoryInterface, TokenStructureInterface}

import com.google.inject.Inject

class StandardTokenStructureFactory @Inject() extends TokenStructureFactoryInterface {

  override def createGroup(tokens: List[TokenInterface]): TokenStructureInterface = new Group(tokens)

  override def createRow(tokens: List[TokenInterface]): TokenStructureInterface = new Row(tokens)
}