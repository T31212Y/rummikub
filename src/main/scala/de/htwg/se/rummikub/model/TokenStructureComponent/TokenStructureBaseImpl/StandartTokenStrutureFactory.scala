package de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.{TokenStructureFactoryInterface, TokenStructureInterface}

object StandartTokenStrutureFactory extends TokenStructureFactoryInterface {
  override def createGroup(tokens: List[TokenInterface]): TokenStructureInterface = Group(tokens)
  override def createRow(tokens: List[TokenInterface]): TokenStructureInterface = Row(tokens)
}

