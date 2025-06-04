package de.htwg.se.rummikub.util.commandComponent.CommandBaseImpl

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.commandComponent.CommandInterface
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingfieldComponent.TokenStackInterface

class AddGroupCommand(
    controller: Controller,
    group: TokenStructureInterface,
    player: PlayerInterface,
    stack: TokenStackInterface
) extends CommandInterface {

  private var oldState: Option[GameState] = None
  private var removedTokens: List[TokenInterface] = List()
  private var executed: Boolean = false

  override def doStep(): Unit = {
    if (!executed) {
      oldState = Some(controller.getState)
      executed = true
    }
    val (tokensRemoved, updatedPlayer) =
      controller.addGroupToTable(group.asInstanceOf[de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.Group], player)
    tokensRemoved.foreach(t => controller.removeTokenFromPlayer(updatedPlayer, t))
    removedTokens = tokensRemoved
  }

  override def undoStep(): Unit = {
    oldState match {
      case Some(state) => controller.setStateInternal(state)
      case None        => println("No State available.")
    }
  }

  override def redoStep(): Unit = doStep()
}
