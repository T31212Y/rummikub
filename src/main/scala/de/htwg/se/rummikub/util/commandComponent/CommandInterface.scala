package de.htwg.se.rummikub.util.commandComponent

trait CommandInterface {
  def doStep(): Unit
  def undoStep(): Unit
  def redoStep(): Unit
}
