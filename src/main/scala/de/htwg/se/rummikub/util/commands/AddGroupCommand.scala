package de.htwg.se.rummikub.util.commands

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Command
import de.htwg.se.rummikub.state.GameState

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

class AddGroupCommand(controller: Controller, group: Group, player: PlayerInterface, stack: TokenStack) extends Command {

  var oldState: Option[GameState] = Some(controller.getState)
  var removedTokens: List[TokenInterface] = List()

  override def doStep(): Unit = {
    val (tokensRemoved, updatedPlayer) = controller.addGroupToTable(group, player)
    tokensRemoved.foreach(t => controller.removeTokenFromPlayer(updatedPlayer, t))
    removedTokens = tokensRemoved
  }

  override def undoStep(): Unit = {
    oldState match {
      case Some(state) =>
        controller.setStateInternal(state)
      case None =>
        println("No State available.")
    }
  }

  override def redoStep(): Unit = {
    doStep()
  }
}