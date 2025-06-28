package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.rummikub.util.Command

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.controller.controllerComponent.{ControllerInterface, GameStateInterface}

class AppendTokenCommand(
    controller: ControllerInterface,
    token: TokenInterface,
    index: Int,
    insertAt: Int,
    isRow: Boolean,
    player: PlayerInterface
) extends Command {

  var oldState: Option[GameStateInterface] = Some(controller.getState)

  override def doStep(): Unit = {
    if (controller.getPlayingField.isEmpty) {
      println("No state available.")
      return
    }

    val currentField = controller.getPlayingField.get.getInnerField
    val currentTokens = currentField.getTokensOnTable

    val updatedTokens = if (index < currentTokens.length) {
      val targetList = currentTokens(index)
      if (insertAt < 0 || insertAt > targetList.length) {
        println(s"Invalid insert position: $insertAt")
        return
      }
      val updatedList = targetList.patch(insertAt, Seq(token), 0)
      currentTokens.updated(index, updatedList)
    } else {
      val emptyListsToAdd = List.fill(index - currentTokens.length + 1)(List.empty[TokenInterface])
      val extendedTokens = currentTokens ++ emptyListsToAdd
      val newList = extendedTokens(index).patch(insertAt, Seq(token), 0)
      extendedTokens.updated(index, newList)
    }

    controller.setPlayingField(controller.getPlayingField.map { pf =>
      pf.updated(
        newPlayers = pf.getPlayers,
        newBoards = pf.getBoards,
        newInnerField = currentField.updated(newTokensOnTable = updatedTokens)
      )
    })

    controller.removeTokenFromPlayer(player, token)
  }

  override def undoStep(): Unit = {
    oldState match {
      case Some(state) =>
        controller.setStateInternal(state)
      case _ =>
        println("No state available.")
    }
  }

  override def redoStep(): Unit = {
    doStep()
  }
}