package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.util.CommandInterface
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.playingfieldComponent.TokenStackInterface

class AddRowCommand(
    controller: ControllerInterface,
    row: TokenStructureInterface,
    player: PlayerInterface,
    stack: TokenStackInterface
) extends CommandInterface {

  private var oldState: Option[GameState] = Some(controller.getState)
  private var removedTokens: List[TokenInterface] = List()

  override def doStep(): Unit = {
    val (tokensRemoved, updatedPlayer) =
      controller.addRowToTable(row.asInstanceOf[de.htwg.se.rummikub.model.tokenStructureComponent.tokenStructureBaseImpl.Row], player)
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
