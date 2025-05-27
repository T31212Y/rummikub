package de.htwg.se.rummikub.aview

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.util.Observer

import scala.io.StdIn.readLine

class Tui(controller: Controller) extends Observer {

    controller.add(this)

    def inputCommands(input: String): Unit = {
        input match {
            case "new" => controller.createNewGame
            case "start" => playGame()
            case "help"  => println(showHelpPage().mkString("\n") + "\n")
            case "quit"  => println(showGoodbye())
            case _       =>
        }
    }

    override def update: Unit = println(controller.playingfieldToString)
}