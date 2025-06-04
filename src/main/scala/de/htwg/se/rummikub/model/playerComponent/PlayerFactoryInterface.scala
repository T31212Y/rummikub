package de.htwg.se.rummikub.model.playerComponent

//import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface

trait PlayerFactoryInterface {
  //def createPlayer(name: String): PlayerInterface
  //def createPlayers(names: List[String]): List[PlayerInterface]
  //def setTokenStructureFactory(factory: TokenStructureFactoryInterface): PlayerFactoryInterface

  def create(name: String, 
  tokens: List[TokenInterface] = List(), 
  commandHistory: List[String] = List(), 
  firstMoveTokens: List[TokenInterface] = List(), 
  hasCompletedFirstMoveFlag: Boolean = false,
  tokenStructureFactory: TokenStructureFactoryInterface
  ): PlayerInterface
}
