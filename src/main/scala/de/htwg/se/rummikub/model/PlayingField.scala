package de.htwg.se.rummikub.model

class PlayingField {
  def logo(): Array[String] = {
    Array(" ____                                _  _            _      _",
          "|  _ \\  _   _  _ __ ___   _ __ ___  (_)| | __ _   _ | |__  | |",
          "| |_) || | | || '_ ` _ \\ | '_ ` _ \\ | || |/ /| | | || '_ \\ | |",
          "|  _ < | |_| || | | | | || | | | | || ||   < | |_| || |_) ||_|",
          "|_| \\_\\\\___,_||_| |_| |_||_| |_| |_||_||_|\\_\\\\___,_||_.__/ (_)"
          )
  }

  val cntRows = 20
  val cntCols = 40
  val cntSpaces = 24

  val edgeUpDown = Array.fill(1, cntCols)("* ")

  val boardP1 = Array.fill(2, 1)(
    "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|" + ("_" * (2 * cntSpaces - 1)) + "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|"
  )
 
  val innerField = Array.fill(cntRows - 4, 1)(
      "|" + (" " * (cntCols * 2 - 3)) + "|"
  )

  val boardEUP2 = Array.fill(1, 1)(
    "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + " " + ("_" * (2 * cntSpaces - 1)) + " " + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|"
  )

  val boardUP2 = Array.fill(1, 1)(
    "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|" + ("_" * (2 * cntSpaces - 1)) + "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|"
  )

  val boardDP2 = Array.fill(1, 1)(
    "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|" + (" " * (2 * cntSpaces - 1)) + "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|"
  )

  def pFieldLayout(): Array[String] = {
    val layout = Array.concat(edgeUpDown, boardP1, innerField, boardEUP2, boardUP2, boardDP2, edgeUpDown)
    layout.map(_.mkString(""))
  }
}