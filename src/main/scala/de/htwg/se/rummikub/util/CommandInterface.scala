package de.htwg.se.rummikub.util

trait CommandInterface {
  def doStep(): Unit
  def undoStep(): Unit
  def redoStep(): Unit
}
