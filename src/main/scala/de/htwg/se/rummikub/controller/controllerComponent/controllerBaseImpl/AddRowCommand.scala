package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.rummikub.util.Command

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.Row
import de.htwg.se.rummikub.model.playingFieldComponent.TokenStackInterface

class AddRowCommand(controller: Controller, row: Row, player: PlayerInterface, stack: TokenStackInterface) extends Command {

  var oldState: Option[GameState] = Some(controller.getState)
  var removedTokens: List[TokenInterface] = List()

  override def doStep(): Unit = {
    val (tokensRemoved, updatedPlayer) = controller.addRowToTable(row, player)
    tokensRemoved.foreach(t => controller.removeTokenFromPlayer(updatedPlayer, t))
    removedTokens = tokensRemoved
  }

  override def undoStep(): Unit = {
    oldState match {
      case Some(state) =>
        controller.setStateInternal(state)
      case _ =>
        println("No State available.")
    }
  }

  override def redoStep(): Unit = {
    doStep()
  }
}