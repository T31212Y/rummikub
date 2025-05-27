package de.htwg.se.rummikub

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.aview._
import de.htwg.se.rummikub.model.GameModeFactory

import scala.io.StdIn.readLine

object Rummikub {
  val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")).get)
  val tui = new Tui(controller)
  val gui = new GuiSwing(controller)

  def main(args: Array[String]): Unit = {
    var input = ""

    println(controller.showWelcome().mkString("\n") + "\n")
    controller.setupNewGame(2, List("Emilia", "Noah"))

    while (input != "quit") {
        println(controller.showHelp() + "\n")
        println("Please enter a command:")
        input = readLine()
        tui.inputCommands(input)
    }
  }
}