package de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.rummikub.util.Command

import de.htwg.se.rummikub.model.playerComponent.PlayerInterface
import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.controller.controllerComponent.{ControllerInterface, GameStateInterface}

class AppendTokenCommand(controller: ControllerInterface, token: TokenInterface, index: Int, isRow: Boolean, player: PlayerInterface) extends Command {

  var oldState: Option[GameStateInterface] = Some(controller.getState)

  override def doStep(): Unit = {
    if (controller.getPlayingField.isEmpty) {
      println("No state available.")
      return
    }
    val updatedTokensOnTable = controller.getPlayingField.get.getInnerField.getTokensOnTable.zipWithIndex.map {
      case (tokenList, i) if i == index => tokenList :+ token
      case (tokenList, _)               => tokenList
    }

    val finalTokensOnTable =
      if (index >= updatedTokensOnTable.length)
        updatedTokensOnTable ++ List.fill(index - updatedTokensOnTable.length + 1)(List()).updated(index, List(token))
      else updatedTokensOnTable

    controller.setPlayingField(controller.getPlayingField.map { pf =>
      pf.updated(newPlayers = pf.getPlayers, newBoards = pf.getBoards, newInnerField = pf.getInnerField.updated(newTokensOnTable = finalTokensOnTable))
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