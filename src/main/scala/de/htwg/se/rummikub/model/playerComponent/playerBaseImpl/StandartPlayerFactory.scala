package de.htwg.se.rummikub.model.playerComponent.playerBaseImpl


import de.htwg.se.rummikub.model.playerComponent.{PlayerInterface, PlayerFactoryInterface}
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureFactoryInterface

class StandardPlayerFactory(private val factory: Option[TokenStructureFactoryInterface] = None) extends PlayerFactoryInterface {

  private var tokenStructureFactory: Option[TokenStructureFactoryInterface] = factory

  override def setTokenStructureFactory(factory: TokenStructureFactoryInterface): PlayerFactoryInterface = {
    tokenStructureFactory = Some(factory)
    this
  }

  override def createPlayer(name: String): PlayerInterface = {
    tokenStructureFactory match {
      case Some(f) => Player(name = name, tokenStructureFactory = f)
      case None => throw new IllegalStateException("TokenStructureFactory must be set before creating players.")
    }
  }

  override def createPlayers(names: List[String]): List[PlayerInterface] = {
    names.map(createPlayer)
  }
}
