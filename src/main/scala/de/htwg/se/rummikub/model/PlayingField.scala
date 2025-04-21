package de.htwg.se.rummikub.model

case class PlayingField(amountOfPlayers: Int, players: List[Player]) {

  val cntRows = 20
  val cntTokens = 24
  val cntEdgeSpaces = 15
 
  val player1 = players(0)
  val player2 = players(1)

  val player3 = if (amountOfPlayers > 2) players(2) else new Player("", List())
  val player4 = if (amountOfPlayers > 3) players(3) else new Player("", List())

  var boardP1 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 1, "up")
  var boardP2 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 1, "down")

  var boardP13 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 2, "up")
  var boardP24 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 2, "down")

  def updatePlayingField(): PlayingField = {
    var pf = new PlayingField(amountOfPlayers, players)
    if (amountOfPlayers == 2) {
      pf.boardP1.boardELRP12_1 = pf.boardP1.formatBoardRow(pf.player1.tokens)
      pf.boardP1.boardELRP12_2 = pf.boardP1.formatEmptyBoardRow(pf.boardP1.size(pf.boardP1.boardELRP12_1) - 4)
      pf.boardP1.boardEUD = pf.boardP1.createBoardFrameSingle(pf.player1.tokens)

      pf.boardP2.boardELRP12_1 = pf.boardP2.formatBoardRow(pf.player2.tokens)
      pf.boardP2.boardELRP12_2 = pf.boardP2.formatEmptyBoardRow(pf.boardP2.size(pf.boardP2.boardELRP12_1) - 4)
      pf.boardP2.boardEUD = pf.boardP2.createBoardFrameSingle(pf.player2.tokens)

    } else if (amountOfPlayers == 3) {
      pf.boardP13.boardELRP12_1 = pf.boardP13.formatBoardRow(pf.player1.tokens)
      pf.boardP13.boardELRP34_1 = pf.boardP13.formatBoardRow(pf.player3.tokens)
      pf.boardP13.boardELRP12_2 = pf.boardP13.formatEmptyBoardRow(pf.boardP13.size(pf.boardP13.boardELRP12_1) - 4)
      pf.boardP13.boardELRP34_2 = pf.boardP13.formatEmptyBoardRow(pf.boardP13.size(pf.boardP13.boardELRP34_1) - 4)
      pf.boardP13.boardEUD = pf.boardP13.createBoardFrameDouble(pf.player1.tokens, pf.player3.tokens)

      pf.boardP2.maxLen = pf.boardP13.size(pf.boardP13.wrapBoardRowDouble(pf.boardP13.boardELRP12_1, pf.boardP13.boardELRP34_1)) - pf.boardP2.size(pf.boardP2.wrapBoardRowSingle(pf.boardP2.boardELRP12_1))

      pf.boardP2.boardELRP12_1 = pf.boardP2.formatBoardRow(pf.player2.tokens)
      pf.boardP2.boardELRP12_2 = pf.boardP2.formatEmptyBoardRow(pf.boardP2.size(pf.boardP2.boardELRP12_1) - 4)
      pf.boardP2.boardEUD = pf.boardP2.createBoardFrameSingle(pf.player2.tokens)

    } else if (amountOfPlayers == 4) {
      pf.boardP13.boardELRP12_1 = pf.boardP13.formatBoardRow(pf.player1.tokens)
      pf.boardP13.boardELRP34_1 = pf.boardP13.formatBoardRow(pf.player3.tokens)
      pf.boardP13.boardELRP12_2 = pf.boardP13.formatEmptyBoardRow(pf.boardP13.size(pf.boardP13.boardELRP12_1) - 4)
      pf.boardP13.boardELRP34_2 = pf.boardP13.formatEmptyBoardRow(pf.boardP13.size(pf.boardP13.boardELRP34_1) - 4)
      pf.boardP13.boardEUD = pf.boardP13.createBoardFrameDouble(pf.player1.tokens, pf.player3.tokens)

      pf.boardP24.boardELRP12_1 = pf.boardP24.formatBoardRow(pf.player2.tokens)
      pf.boardP24.boardELRP34_1 = pf.boardP24.formatBoardRow(pf.player4.tokens)
      pf.boardP24.boardELRP12_2 = pf.boardP24.formatEmptyBoardRow(pf.boardP24.size(pf.boardP24.boardELRP12_1) - 4)
      pf.boardP24.boardELRP34_2 = pf.boardP24.formatEmptyBoardRow(pf.boardP24.size(pf.boardP24.boardELRP34_1) - 4)
      pf.boardP24.boardEUD = pf.boardP24.createBoardFrameDouble(pf.player2.tokens, pf.player4.tokens)
    }
    pf
  }

  def lineWithPlayerName(char: String, length: Int, p: String): String = {
    val len = length - p.length - 5
    char.repeat(5) + p + char * len
  }

  def lineWith2PlayerNames(char: String, length: Int, p1: String, p2: String): String = {
    val len = length - p1.length - p2.length - 10
    char.repeat(5) + p1 + char * len + p2 + char.repeat(5)
  }

  def simpleLine(char: String, length: Int): String = {
    char * length
  }

  override def toString(): String = {
    var playingField = ""
    if (amountOfPlayers == 2) { 
      val edgeUp = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player1.name) + "\n"
      val innerField = "|" + simpleLine(" ", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)) - 2) + "|\n"
      val edgeDown = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player2.name) + "\n"

      playingField = edgeUp + boardP1.toString() + innerField.repeat(cntRows - 4) + boardP2.toString() + edgeDown + "\n"
    } else if (amountOfPlayers == 3) {
      val edgeUp = lineWith2PlayerNames("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player1.name, player3.name) + "\n"
      val innerField = "|" + simpleLine(" ", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)) - 2) + "|\n"
      val edgeDown = lineWithPlayerName("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player2.name) + "\n"

      playingField = edgeUp + boardP13.toString() + innerField.repeat(cntRows - 4) + boardP2.toString() + edgeDown + "\n"
    } else if (amountOfPlayers == 4) {
      val edgeUp = lineWith2PlayerNames("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player1.name, player3.name) + "\n"
      val innerField = "|" + simpleLine(" ", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)) - 2) + "|\n"
      val edgeDown = lineWith2PlayerNames("*", boardP24.size(boardP24.wrapBoardRowDouble(boardP24.boardELRP12_1, boardP24.boardELRP34_1)), player2.name, player4.name) + "\n"

      playingField = edgeUp + boardP13.toString() + innerField.repeat(cntRows - 4) + boardP24.toString() + edgeDown + "\n"
    }

    playingField
  }
}