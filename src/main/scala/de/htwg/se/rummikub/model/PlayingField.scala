package de.htwg.se.rummikub.model

case class PlayingField(amountOfPlayers: Int, players: List[Player]) {

  val cntRows = 20
  var cntCols = if (amountOfPlayers > 2) 140 else 80
  val cntSpaces = 24
 
  val player1 = players(0)
  val player2 = players(1)

  val player3 = if (amountOfPlayers > 2) players(2) else new Player("", List())
  val player4 = if (amountOfPlayers > 3) players(3) else new Player("", List())

  val edgeUpDown = "*" * (cntCols - 1) + "\n"

  var boardELRP1_1 = "|" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "| " + ("x " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "|\n"
  var boardELRP1_2 = "|" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "| " + ("x " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "|\n"
  val boardEUP1 = "|" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "+" + ("-" * (2 * cntSpaces - 1)) + "+" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "|\n"

  var boardELRP13_1 = "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "| " + ("x " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "| " + ("y " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "|\n"
  var boardELRP13_2 = "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "| " + ("x " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "| " + ("y " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "|\n"
  val boardEUP13 = "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "+" + ("-" * (2 * cntSpaces - 1)) + "+" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "+" + ("-" * (2 * cntSpaces - 1)) + "+" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "|\n"

  val innerField = "|" + (" " * (cntCols - 3)) + "|\n"

  val boardEUP2 = "|" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "+" + ("-" * (2 * cntSpaces - 1)) + "+" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "|\n"
  var boardELRP2_1 = "|" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "| " + ("x " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "|\n"
  var boardELRP2_2 = "|" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "| " + ("x " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 3) / 2 - cntSpaces)) + "|\n"

  val boardEUP24 = "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "+" + ("-" * (2 * cntSpaces - 1)) + "+" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "+" + ("-" * (2 * cntSpaces - 1)) + "+" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "|\n"
  var boardELRP24_1 = "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "| " + ("x " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "| " + ("y " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "|\n"
  var boardELRP24_2 = "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "| " + ("x " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "| " + ("y " * (cntSpaces - 1)) + "|" + (" " * ((cntCols - 63) / 2 - cntSpaces - 1)) + "|\n"

  def updatePlayingField(): PlayingField = {
    var pf = new PlayingField(amountOfPlayers, players)
    if (amountOfPlayers == 2) {
      var iP1 = 0
      pf.player1.tokens.foreach { token => if (iP1 < 24) {
        pf.boardELRP1_1 = pf.boardELRP1_1.replaceFirst("x", token.toString())
      } else {
        pf.boardELRP1_2 = pf.boardELRP1_2.replaceFirst("x", token.toString())
      }
        iP1 += 1        
      }

      var iP2 = 0
      pf.player2.tokens.foreach { token => if (iP2 < 24) {
        pf.boardELRP2_1 = pf.boardELRP2_1.replaceFirst("x", token.toString())
      } else {
        pf.boardELRP2_2 = pf.boardELRP2_2.replaceFirst("x", token.toString())
      }
        iP2 += 1
      }
    } else if (amountOfPlayers == 3) {
      var iP1 = 0
      pf.player1.tokens.foreach { token => if (iP1 < 24) {
        pf.boardELRP13_1 = pf.boardELRP13_1.replaceFirst("x", token.toString())
      } else {
        pf.boardELRP13_2 = pf.boardELRP13_2.replaceFirst("x", token.toString())
      }
        iP1 += 1
      }

      var iP2 = 0
      pf.player2.tokens.foreach { token => if (iP2 < 24) {
        pf.boardELRP2_1 = pf.boardELRP2_1.replaceFirst("x", token.toString())
      } else {
        pf.boardELRP2_2 = pf.boardELRP2_2.replaceFirst("x", token.toString())
      }
        iP2 += 1
      }

      var iP3 = 0
      pf.player3.tokens.foreach { token => if (iP3 < 24) {
        pf.boardELRP13_1 = pf.boardELRP13_1.replaceFirst("y", token.toString())
      } else {
        pf.boardELRP13_2 = pf.boardELRP13_2.replaceFirst("y", token.toString())
      }
        iP3 += 1
      }
    } else if (amountOfPlayers == 4) {
      var iP1 = 0
      pf.player1.tokens.foreach { token => if (iP1 < 24) {
        pf.boardELRP13_1 = pf.boardELRP13_1.replaceFirst("x", token.toString())
      } else {
        pf.boardELRP13_2 = pf.boardELRP13_2.replaceFirst("x", token.toString())
      }
        iP1 += 1
      }

      var iP2 = 0
      pf.player2.tokens.foreach { token => if (iP2 < 24) {
        pf.boardELRP24_1 = pf.boardELRP24_1.replaceFirst("x", token.toString())
      } else {
        pf.boardELRP24_2 = pf.boardELRP24_2.replaceFirst("x", token.toString())
      }
        iP2 += 1
      }

      var iP3 = 0
      pf.player3.tokens.foreach { token => if (iP3 < 24) {
        pf.boardELRP13_1 = pf.boardELRP13_1.replaceFirst("y", token.toString())
      } else {
        pf.boardELRP13_2 = pf.boardELRP13_2.replaceFirst("y", token.toString())
      }
        iP3 += 1
      }

      var iP4 = 0
      pf.player4.tokens.foreach { token => if (iP4 < 24) {
        pf.boardELRP24_1 = pf.boardELRP24_1.replaceFirst("y", token.toString())
      } else {
        pf.boardELRP24_2 = pf.boardELRP24_2.replaceFirst("y", token.toString())
      }
        iP4 += 1
      }
    }
    pf
  }

  override def toString(): String = {
    var playingField = ""

    if (amountOfPlayers == 2) {
      playingField = " ".repeat(15) + "***" + player1.name + "\n" + edgeUpDown + boardELRP1_1 + boardEUP1 + boardELRP1_2 + boardEUP1 + innerField.repeat(cntRows - 4) + boardEUP2 + boardELRP2_1 + boardEUP2 + boardELRP2_2 + edgeUpDown + " ".repeat(15) + "***" + player2.name + "\n"
    } else if (amountOfPlayers == 3) {
      playingField = " ".repeat(14) + "***" + player1.name + " ".repeat(55) + "***" + player2.name + "\n" + edgeUpDown + boardELRP13_1 + boardEUP13 + boardELRP13_2 + boardEUP13 + innerField.repeat(cntRows - 4) + boardEUP2 + boardELRP2_1 + boardEUP2 + boardELRP2_2 + edgeUpDown + " ".repeat(45) + "***" + player3.name + "\n"
    } else if (amountOfPlayers == 4) {
      playingField = " ".repeat(14) + "***" + player1.name + " ".repeat(55) + "***" + player2.name + "\n" + edgeUpDown + boardELRP13_1 + boardEUP13 + boardELRP13_2 + boardEUP13 + innerField.repeat(cntRows - 4) + boardEUP24 + boardELRP24_1 + boardEUP24 + boardELRP24_2 + edgeUpDown + " ".repeat(14) + "***" + player3.name + " ".repeat(55) + "***" + player4.name + "\n"
    }

    playingField
  }
}