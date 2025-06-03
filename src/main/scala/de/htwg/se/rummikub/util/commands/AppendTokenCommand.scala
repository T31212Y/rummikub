package de.htwg.se.rummikub.util.commands

import de.htwg.se.rummikub.model._
import de.htwg.se.rummikub.util.Command
import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.model.playerComponent.playerBaseImpl.Player
import de.htwg.se.rummikub.model.tokenComponent.Token

class AppendTokenCommand(controller: Controller, token: Token, index: Int, isRow: Boolean, player: Player) extends Command {

  var oldState: Option[GameState] = Some(controller.getState)

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