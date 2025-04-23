package de.htwg.se.rummikub

import de.htwg.se.rummikub.model.{Player, PlayingField, TokenStack}
import de.htwg.se.rummikub.aview.Tui

import scala.io.StdIn.readLine

object Rummikub {
  val tui = new Tui()

  def main(args: Array[String]): Unit = {
    println(tui.showWelcome().mkString("\n") + "\n")

    println(tui.askAmountOfPlayers())
    val amountPlayers: Int = readLine().toInt

    println(tui.askPlayerNames())
    val players = readLine().split(",").map(_.trim).toList.map(name => Player(name))
    println("\n")

    var playingField = new PlayingField(amountPlayers, players)
    var input = ""
    
    while (input != "quit") {
      println(playingField.toString().replace("x", " ").replace("y", " ") + "\n")
      print(tui.showHelp() + "\n")
      println("Please enter a command:")
      input = readLine()
      playingField = tui.inputCommands(input, playingField)
    }

    println(tui.showGoodbye())
  }
}