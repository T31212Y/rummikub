package de.htwg.se.rummikub

import de.htwg.se.rummikub.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.aview._
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory

import scala.io.StdIn.readLine
import de.htwg.se.rummikub.aview.gui.Gui

object Rummikub {
  val gameModeFactory = new GameModeFactory
  val controller = new Controller(gameModeFactory.createGameMode(2, List("Emilia", "Noah")).get, gameModeFactory)

  given ControllerInterface = controller

  val tui: GameView = new Tui
  val gui: GameView = new Gui

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
