package de.htwg.se.rummikub.model.playerComponent


import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface

trait PlayerFactoryInterface {
  def createPlayer(name: String): PlayerInterface
  def createPlayers(names: List[String]): List[PlayerInterface]
  def setTokenStructureFactory(factory: TokenStructureFactoryInterface): PlayerFactoryInterface
}
