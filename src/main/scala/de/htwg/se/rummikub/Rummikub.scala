package de.htwg.se.rummikub

import de.htwg.se.rummikub.model.{Player, PlayingField}
import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.aview.Tui

import scala.io.StdIn.readLine

object Rummikub {
  val controller = new Controller(new PlayingField(2, List(new Player("Emilia"), new Player("Noah"))))
  val tui = new Tui(controller)

  def main(args: Array[String]): Unit = {
    var input = ""

    println(tui.showWelcome().mkString("\n") + "\n")
    controller.notifyObservers
    
    while (input != "quit") {
      print(tui.showHelp() + "\n")
      println("Please enter a command:")
      input = readLine()
      tui.inputCommands(input)
    }

    println(tui.showGoodbye())
  }
}