package de.htwg.se.rummikub

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.aview._
import de.htwg.se.rummikub.model.gameModeComponent.GameModeFactory

import scala.io.StdIn.readLine
import de.htwg.se.rummikub.aview.gui.Gui

object Rummikub {
  val controller = new ControllerInterface(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
  val tui: GameView = new Tui(controller)
  val gui: GameView = new Gui(controller)

  def main(args: Array[String]): Unit = {
    var input = ""

    println(tui.showWelcome.mkString("\n") + "\n")
    controller.setupNewGame(2, List("Emilia", "Noah"))

    while (input != "quit") {
        println(tui.showHelp)
        println("Please enter a command:")
        input = readLine()
        tui.inputCommands(input)
    }
  }
}