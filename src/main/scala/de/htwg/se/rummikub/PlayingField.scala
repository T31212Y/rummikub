package de.htwg.se.rummikub

class PlayingField {
  def logo(): Unit = {
    println(" ____                                _  _            _      _")
    println("|  _ \\  _   _  _ __ ___   _ __ ___  (_)| | __ _   _ | |__  | |")
    println("| |_) || | | || '_ ` _ \\ | '_ ` _ \\ | || |/ /| | | || '_ \\ | |")
    println("|  _ < | |_| || | | | | || | | | | || ||   < | |_| || |_) ||_|")
    println("|_| \\_\\\\___,_||_| |_| |_||_| |_| |_||_||_|\\_\\\\___,_||_.__/ (_)")
    println("")
  }

  val cntRows = 20
  val cntCols = 40
  val cntSpaces = 24

  val edgeUpDown = Array.fill(1, cntCols)("*")

  val boardP1 = Array.fill(2)(
    "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|" + ("_" * (2 * cntSpaces - 1)) + "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|"
  )
 
  val innerField = Array.fill(cntRows - 4)(
      "|" + (" " * (cntCols * 2 - 3)) + "|"
  )

  val boardEUP2 = Array.fill(1)(
    "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + " " + ("_" * (2 * cntSpaces - 1)) + " " + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|"
  )

  val boardUP2 = Array.fill(1)(
    "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|" + ("_" * (2 * cntSpaces - 1)) + "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|"
  )

  val boardDP2 = Array.fill(1)(
    "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|" + (" " * (2 * cntSpaces - 1)) + "|" + (" " * ((cntCols * 2 - 3) / 2 - cntSpaces)) + "|"
  )

  def pFieldLayout(): Unit = {
    println(edgeUpDown.map(_.mkString(" ")).mkString("\n"))
    println(boardP1.map(_.mkString("")).mkString("\n"))
    println(innerField.map(_.mkString("")).mkString("\n"))
    println(boardEUP2.map(_.mkString("")).mkString("\n"))
    println(boardUP2.map(_.mkString("")).mkString("\n"))
    println(boardDP2.map(_.mkString("")).mkString("\n"))
    println(edgeUpDown.map(_.mkString(" ")).mkString("\n"))
  }
}