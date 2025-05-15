package de.htwg.se.rummikub.aview

trait GameView {
  def showWelcome(): Vector[String]
  def showHelp(): String
  def showHelpPage(): Vector[String]
  def showGoodbye(): String
  def start(): Unit
}