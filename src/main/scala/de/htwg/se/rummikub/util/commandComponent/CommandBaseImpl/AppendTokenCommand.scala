package de.htwg.se.rummikub.util.commandComponent.CommandBaseImpl

import de.htwg.se.rummikub.util.commandComponent.CommandInterface
import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface
import de.htwg.se.rummikub.model.playingfieldComponent.{PlayingFieldInterface, TableInterface}

class AppendTokenCommand(
    controller: ControllerInterface,
    token: TokenInterface,
    index: Int,
    isRow: Boolean,
    player: PlayerInterface
) extends CommandInterface {

  private var oldState: Option[GameState] = Some(controller.getState)

  override def doStep(): Unit = {
    if (controller.playingField.isEmpty || controller.gameState.isEmpty) {
      println("No state available.")
      return
    }

    val pf = controller.playingField.get
    val table = pf.getInnerField
    val tokensOnTable = table.getRow(index).getOrElse(List())
    val updatedRow = tokensOnTable :+ token.asInstanceOf[TokenStructureInterface]

    val updatedTable = table.updateRow(index, updatedRow)
    controller.setPlayingField(Some(pf.setInnerField(updatedTable)))
    controller.removeTokenFromPlayer(player, token)
  }

  override def undoStep(): Unit = {
    oldState match {
      case Some(state) => controller.setStateInternal(state)
      case None        => println("No State available.")
    }
  }

  override def redoStep(): Unit = doStep()
}
