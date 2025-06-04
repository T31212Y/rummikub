package de.htwg.se.rummikub.util.commandComponent.CommandBaseImpl

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.util.commandComponent.CommandInterface
import de.htwg.se.rummikub.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

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

    val updatedTokensOnTable = controller.playingField.get.innerField.tokensOnTable.zipWithIndex.map {
      case (tokenList, i) if i == index => tokenList :+ token
      case (tokenList, _)               => tokenList
    }

    val finalTokensOnTable =
      if (index >= updatedTokensOnTable.length)
        updatedTokensOnTable ++ List.fill(index - updatedTokensOnTable.length + 1)(List()).updated(index, List(token))
      else updatedTokensOnTable

    controller.playingField = controller.playingField.map { pf =>
      pf.copy(innerField = pf.innerField.copy(tokensOnTable = finalTokensOnTable))
    }

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
