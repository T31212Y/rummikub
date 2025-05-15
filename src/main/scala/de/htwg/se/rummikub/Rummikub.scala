package de.htwg.se.rummikub

import de.htwg.se.rummikub.controller.Controller
import de.htwg.se.rummikub.aview._
import de.htwg.se.rummikub.model.GameModeFactory

import scala.io.StdIn.readLine

object Rummikub {
  val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")))
  val view: GameView = new Tui(controller)

  def main(args: Array[String]): Unit = {
    view.start()
  }
}