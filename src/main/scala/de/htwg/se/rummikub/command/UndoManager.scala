package de.htwg.rummikub.command

import scala.util.Try
import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.state.GameState
import de.htwg.se.rummikub.command.Command

class UndoManager(controller: Controller) {
  private var undoStack: List[GameState] = Nil
  private var redoStack: List[GameState] = Nil
  
  def execute(cmd: Command): Try[GameState] = {
    val oldState = controller.getState
    cmd.execute(oldState).map { newState =>
      undoStack = oldState :: undoStack
      redoStack = Nil
      controller.setStateInternal(newState)
      newState
    }
  }
  def undo(): Option[GameState] = undoStack match {
    case prev :: rest =>
      val current = controller.getState
      redoStack = current :: redoStack
      controller.setStateInternal(prev)
      undoStack = rest
      Some(prev)
    case Nil =>
      None
  }
  def redo(): Option[GameState] = redoStack match {
    case next :: rest =>
      val current = controller.getState
      undoStack = current :: undoStack
      controller.setStateInternal(next)
      redoStack = rest
      Some(next)
    case Nil =>
      None
  }
}