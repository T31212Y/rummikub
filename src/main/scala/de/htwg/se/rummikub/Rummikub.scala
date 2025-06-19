package de.htwg.se.rummikub

import de.htwg.se.rummikub.aview.{GameView, Tui}
import de.htwg.se.rummikub.aview.gui.Gui

import de.htwg.se.rummikub.RummikubDependencyModule.given

import scala.io.StdIn.readLine

object Rummikub {
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