package de.htwg.se.rummikub.util.commands

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Command
import de.htwg.se.rummikub.state.GameState

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

class AddRowCommand(controller: Controller, row: Row, player: PlayerInterface, stack: TokenStack) extends Command {

  var oldState: Option[GameState] = Some(controller.getState)
  var removedTokens: List[Token] = List()

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