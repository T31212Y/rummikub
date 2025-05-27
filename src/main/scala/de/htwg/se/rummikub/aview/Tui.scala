package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Observer

import scala.io.StdIn.readLine

class Tui(controller: Controller) extends Observer {

    controller.add(this)

    def inputCommands(input: String): Unit = {
        input match {
            case "new" => controller.createNewGame
            case "start" => controller.playGame()
            case "help"  => println(controller.showHelpPage().mkString("\n") + "\n")
            case "quit"  => println(controller.showGoodbye())
            case _       =>
        }
    }

    override def update: Unit = println(controller.playingfieldToString)
}