package de.htwg.se.rummikub.model.tokenStructureComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

trait TokenStructureFactoryInterface {
  def createGroup(tokens: List[TokenInterface]): TokenStructureInterface
  def createRow(tokens: List[TokenInterface]): TokenStructureInterface
}