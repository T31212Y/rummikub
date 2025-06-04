package de.htwg.se.rummikub.util
import de.htwg.se.rummikub.util.CommandInterface


class UndoManager {
  private var undoStack: List[CommandInterface] = Nil
  private var redoStack: List[CommandInterface] = Nil

  def doStep(cmd: CommandInterface) = {
    undoStack = cmd::undoStack
    cmd.doStep()
  }

  def undoStep() = {
    undoStack match {
      case Nil =>
      case head::stack => {
        head.undoStep()
        undoStack = stack
        redoStack = head::redoStack
      }
    }
  }
  
  def redoStep() = {
    redoStack match {
      case Nil =>
      case head::stack => {
        head.redoStep()
        redoStack = stack
        undoStack = head::undoStack
      }
    }
  }
}