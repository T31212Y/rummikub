package de.htwg.se.rummikub.model

case class PlayingField(amountOfPlayers: Int, players: List[Player]) {

  val cntRows = 20
  val cntTokens = 24
  val cntEdgeSpaces = 15
 
  val player1 = players(0)
  val player2 = players(1)

  val player3 = if (amountOfPlayers > 2) players(2) else new Player("")
  val player4 = if (amountOfPlayers > 3) players(3) else new Player("")

  var boardP1 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 1, "up")
  var boardP2 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 1, "down")

  var boardP13 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 2, "up")
  var boardP24 = new Board(cntEdgeSpaces, cntTokens, amountOfPlayers, 2, "down")

  var innerField2Players = new Table(cntRows - 4, boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)) - 2)
  var innerField34Players = new Table(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)) - 2)

  def updatePlayingField(): PlayingField = {
    var pf = new PlayingField(amountOfPlayers, players)
    if (amountOfPlayers == 2) {
      if (player1.tokens.size < cntTokens + 1) {
        pf.boardP1.boardELRP12_1 = pf.boardP1.formatBoardRow(pf.player1.tokens)
        pf.boardP1.boardELRP12_2 = pf.boardP1.formatEmptyBoardRow(pf.boardP1.size(pf.boardP1.boardELRP12_1) - 4)
        pf.boardP1.boardEUD = pf.boardP1.createBoardFrameSingle(pf.player1.tokens)
      } else {
        pf.boardP1.boardELRP12_1 = pf.boardP1.formatBoardRow(pf.player1.tokens.take(cntTokens))
        pf.boardP1.boardELRP12_2 = pf.boardP1.formatBoardRow(pf.player1.tokens.drop(cntTokens))
        pf.boardP1.boardEUD = pf.boardP1.createBoardFrameSingle(pf.player1.tokens.take(cntTokens))
      }

      if (player2.tokens.size < cntTokens + 1) {
        pf.boardP2.boardELRP12_1 = pf.boardP2.formatBoardRow(pf.player2.tokens)
        pf.boardP2.boardELRP12_2 = pf.boardP2.formatEmptyBoardRow(pf.boardP2.size(pf.boardP2.boardELRP12_1) - 4)
        pf.boardP2.boardEUD = pf.boardP2.createBoardFrameSingle(pf.player2.tokens)
      } else {
        pf.boardP2.boardELRP12_1 = pf.boardP2.formatBoardRow(pf.player2.tokens.take(cntTokens))
        pf.boardP2.boardELRP12_2 = pf.boardP2.formatBoardRow(pf.player2.tokens.drop(cntTokens))
        pf.boardP2.boardEUD = pf.boardP2.createBoardFrameSingle(pf.player2.tokens.take(cntTokens))
      }

      pf.innerField2Players = new Table(cntRows - 4, pf.boardP1.size(pf.boardP1.wrapBoardRowSingle(pf.boardP1.boardELRP12_1)) - 2)
      pf.innerField2Players.tokensOnTable = this.innerField2Players.tokensOnTable

    } else if (amountOfPlayers == 3) {
      if (player1.tokens.size < cntTokens + 1) {
        pf.boardP13.boardELRP12_1 = pf.boardP13.formatBoardRow(pf.player1.tokens)
        pf.boardP13.boardELRP12_2 = pf.boardP13.formatEmptyBoardRow(pf.boardP13.size(pf.boardP13.boardELRP12_1) - 4)
      } else {
        pf.boardP13.boardELRP12_1 = pf.boardP13.formatBoardRow(pf.player1.tokens.take(cntTokens))
        pf.boardP13.boardELRP12_2 = pf.boardP13.formatBoardRow(pf.player1.tokens.drop(cntTokens))
      }

      if (player3.tokens.size < cntTokens + 1) {
        pf.boardP13.boardELRP34_1 = pf.boardP13.formatBoardRow(pf.player3.tokens)
        pf.boardP13.boardELRP34_2 = pf.boardP13.formatEmptyBoardRow(pf.boardP13.size(pf.boardP13.boardELRP34_1) - 4)
      } else {
        pf.boardP13.boardELRP34_1 = pf.boardP13.formatBoardRow(pf.player3.tokens.take(cntTokens))
        pf.boardP13.boardELRP34_2 = pf.boardP13.formatBoardRow(pf.player3.tokens.drop(cntTokens))
      }

      pf.boardP13.boardEUD = pf.boardP13.createBoardFrameDouble(pf.player1.tokens.take(cntTokens), pf.player3.tokens.take(cntTokens))
      pf.boardP2.maxLen = pf.boardP13.size(pf.boardP13.wrapBoardRowDouble(pf.boardP13.boardELRP12_1, pf.boardP13.boardELRP34_1)) - pf.boardP2.size(pf.boardP2.wrapBoardRowSingle(pf.boardP2.boardELRP12_1))

      if (player2.tokens.size < cntTokens + 1) {
        pf.boardP2.boardELRP12_1 = pf.boardP2.formatBoardRow(pf.player2.tokens)
        pf.boardP2.boardELRP12_2 = pf.boardP2.formatEmptyBoardRow(pf.boardP2.size(pf.boardP2.boardELRP12_1) - 4)
        pf.boardP2.boardEUD = pf.boardP2.createBoardFrameSingle(pf.player2.tokens)
      } else {
        pf.boardP2.boardELRP12_1 = pf.boardP2.formatBoardRow(pf.player2.tokens.take(cntTokens))
        pf.boardP2.boardELRP12_2 = pf.boardP2.formatBoardRow(pf.player2.tokens.drop(cntTokens))
        pf.boardP2.boardEUD = pf.boardP2.createBoardFrameSingle(pf.player2.tokens.take(cntTokens))
      }

      pf.innerField34Players = new Table(cntRows - 4, pf.boardP13.size(pf.boardP13.wrapBoardRowDouble(pf.boardP13.boardELRP12_1, pf.boardP13.boardELRP34_1)) - 2)
      pf.innerField34Players.tokensOnTable = this.innerField34Players.tokensOnTable      

    } else if (amountOfPlayers == 4) {
      if (player1.tokens.size < cntTokens + 1) {
        pf.boardP13.boardELRP12_1 = pf.boardP13.formatBoardRow(pf.player1.tokens)
        pf.boardP13.boardELRP12_2 = pf.boardP13.formatEmptyBoardRow(pf.boardP13.size(pf.boardP13.boardELRP12_1) - 4)
      } else {
        pf.boardP13.boardELRP12_1 = pf.boardP13.formatBoardRow(pf.player1.tokens.take(cntTokens))
        pf.boardP13.boardELRP12_2 = pf.boardP13.formatBoardRow(pf.player1.tokens.drop(cntTokens))
      }

      if (player3.tokens.size < cntTokens + 1) {
        pf.boardP13.boardELRP34_1 = pf.boardP13.formatBoardRow(pf.player3.tokens)
        pf.boardP13.boardELRP34_2 = pf.boardP13.formatEmptyBoardRow(pf.boardP13.size(pf.boardP13.boardELRP34_1) - 4)
      } else {
        pf.boardP13.boardELRP34_1 = pf.boardP13.formatBoardRow(pf.player3.tokens.take(cntTokens))
        pf.boardP13.boardELRP34_2 = pf.boardP13.formatBoardRow(pf.player3.tokens.drop(cntTokens))
      }

      pf.boardP13.boardEUD = pf.boardP13.createBoardFrameDouble(pf.player1.tokens.take(cntTokens), pf.player3.tokens.take(cntTokens))

      if (player2.tokens.size < cntTokens + 1) {
        pf.boardP24.boardELRP12_1 = pf.boardP24.formatBoardRow(pf.player2.tokens)
        pf.boardP24.boardELRP12_2 = pf.boardP24.formatEmptyBoardRow(pf.boardP24.size(pf.boardP24.boardELRP12_1) - 4)
      } else {
        pf.boardP24.boardELRP12_1 = pf.boardP24.formatBoardRow(pf.player2.tokens.take(cntTokens))
        pf.boardP24.boardELRP12_2 = pf.boardP24.formatBoardRow(pf.player2.tokens.drop(cntTokens))
      }

      if (player4.tokens.size < cntTokens + 1) {
        pf.boardP24.boardELRP34_1 = pf.boardP24.formatBoardRow(pf.player4.tokens)
        pf.boardP24.boardELRP34_2 = pf.boardP24.formatEmptyBoardRow(pf.boardP24.size(pf.boardP24.boardELRP34_1) - 4)
      } else {
        pf.boardP24.boardELRP34_1 = pf.boardP24.formatBoardRow(pf.player4.tokens.take(cntTokens))
        pf.boardP24.boardELRP34_2 = pf.boardP24.formatBoardRow(pf.player4.tokens.drop(cntTokens))
      }

      pf.boardP24.boardEUD = pf.boardP24.createBoardFrameDouble(pf.player2.tokens.take(cntTokens), pf.player4.tokens.take(cntTokens))

      pf.innerField34Players = new Table(cntRows - 4, pf.boardP24.size(pf.boardP24.wrapBoardRowDouble(pf.boardP24.boardELRP12_1, pf.boardP24.boardELRP34_1)) - 2)
      pf.innerField34Players.tokensOnTable = this.innerField34Players.tokensOnTable
      
    }
    pf
  }

  def addTableRow(row: Row): List[Token | Joker] = {
    if (amountOfPlayers == 2) {
      innerField2Players.tokensOnTable = innerField2Players.tokensOnTable :+ row.rowTokens
    } else if (amountOfPlayers > 2) {
      innerField34Players.tokensOnTable = innerField34Players.tokensOnTable :+ row.rowTokens
    }
    row.rowTokens
  }

  def addTableGroup(group: Group): List[Token | Joker] = {
    if (amountOfPlayers == 2) {
      innerField2Players.tokensOnTable = innerField2Players.tokensOnTable :+ group.groupTokens
    } else if (amountOfPlayers > 2) {
      innerField34Players.tokensOnTable = innerField34Players.tokensOnTable :+ group.groupTokens
    }
    group.groupTokens
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

  def nextPlayer(p: Player): Player = {
    val current = players.indexOf(p)
    if (current == players.size - 1) players(0) else players(current + 1)
  }

  override def toString(): String = {
    var playingField = ""
    if (amountOfPlayers == 2) { 
      val edgeUp = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player1.name) + "\n"
      val edgeDown = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player2.name) + "\n"

      playingField = edgeUp + boardP1.toString() + innerField2Players.toString() + boardP2.toString() + edgeDown + "\n"
    } else if (amountOfPlayers == 3) {
      val edgeUp = lineWith2PlayerNames("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player1.name, player3.name) + "\n"
      val edgeDown = lineWithPlayerName("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player2.name) + "\n"

      playingField = edgeUp + boardP13.toString() + innerField34Players.toString() + boardP2.toString() + edgeDown + "\n"
    } else if (amountOfPlayers == 4) {
      val edgeUp = lineWith2PlayerNames("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player1.name, player3.name) + "\n"
      val edgeDown = lineWith2PlayerNames("*", boardP24.size(boardP24.wrapBoardRowDouble(boardP24.boardELRP12_1, boardP24.boardELRP34_1)), player2.name, player4.name) + "\n"

      playingField = edgeUp + boardP13.toString() + innerField34Players.toString() + boardP24.toString() + edgeDown + "\n"
    }
    playingField
  }
}