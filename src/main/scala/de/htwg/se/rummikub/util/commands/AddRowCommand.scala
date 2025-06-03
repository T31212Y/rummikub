package de.htwg.se.rummikub.util.commands

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Command
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface
import de.htwg.se.rummikub.model.playingfieldComponent.PlayingFieldInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface

class AddRowCommand(controller: Controller, row: TokenStructureInterface, player: PlayerInterface, stack: PlayingFieldInterface) extends Command {

  var oldState: Option[GameState] = Some(controller.getState)
  var removedTokens: List[TokenInterface] = List()

  override def doStep(): Unit = {
    val (tokensRemoved, updatedPlayer) = controller.addRowToTable(row.asInstanceOf[de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.Row], player)
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