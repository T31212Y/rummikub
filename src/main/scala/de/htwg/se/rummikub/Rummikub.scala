package de.htwg.se.rummikub

import de.htwg.se.rummikub.aview.{GameView, Tui}
import de.htwg.se.rummikub.aview.gui.Gui

import de.htwg.se.rummikub.model.gameModeComponent.GameModeFactoryInterface
import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface

import com.google.inject.Guice

import scala.io.StdIn.readLine

object Rummikub {
  val injector = Guice.createInjector(new RummikubModule)

  val gameModeFactory = injector.getInstance(classOf[GameModeFactoryInterface])
  val controller = injector.getInstance(classOf[ControllerInterface])

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