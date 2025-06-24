package de.htwg.se.rummikub

import de.htwg.se.rummikub.aview.{GameView, Tui}
import de.htwg.se.rummikub.aview.gui.Gui

import de.htwg.se.rummikub.controller.controllerComponent.ControllerInterface
import de.htwg.se.rummikub.model.tokenComponent.{Color, TokenFactoryInterface}
import de.htwg.se.rummikub.model.fileIoComponent.FileIOInterface

import com.google.inject.Guice

import scala.io.StdIn.readLine

object Rummikub {
  val injector = Guice.createInjector(new RummikubModule)
  val controller = injector.getInstance(classOf[ControllerInterface])

  val fileIO = injector.getInstance(classOf[FileIOInterface])
  val tokenFactory = injector.getInstance(classOf[TokenFactoryInterface])

  val tui: GameView = new Tui(controller)
  val gui: GameView = new Gui(controller)

  def main(args: Array[String]): Unit = {
    var input = ""

    println(tui.showWelcome.mkString("\n") + "\n")
    controller.setupNewGame(2, List("Emilia", "Noah"))

    val token = tokenFactory.createNumToken(5, Color.RED)
    fileIO.to(token)

    val loaded = fileIO.from
    println("Aus FileIO geladener Token: " + loaded)

    while (input != "quit") {
        println(tui.showHelp)
        println("Please enter a command:")
        input = readLine()
        tui.inputCommands(input)
    }
  }
}