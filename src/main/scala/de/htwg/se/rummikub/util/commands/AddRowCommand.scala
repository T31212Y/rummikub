package de.htwg.se.rummikub.util.commands

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Command
import de.htwg.se.rummikub.state.GameState

class AddRowCommand(controller: Controller, row: Row, player: Player, stack: TokenStack) extends Command {
  var oldState: Option[GameState] = None
  var removedTokens: List[Token] = List()

  override def doStep(): Unit = {
    oldState = controller.gameState

    val (tokensRemoved, updatedPlayer) = controller.addRowToTable(row, player)
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